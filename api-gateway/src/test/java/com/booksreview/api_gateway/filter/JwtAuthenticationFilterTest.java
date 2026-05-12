package com.portfolio.gateway.filter;

import com.portfolio.gateway.security.JwtTokenValidator;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.List;

/**
 * Tests de integración del filtro JWT.
 * Usa WebTestClient para hacer requests reactivos reales contra el gateway.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class JwtAuthenticationFilterTest {

    @LocalServerPort
    private int port;

    @Autowired
    private WebTestClient webTestClient;

    private static final String SECRET = "test-secret-key-for-testing-only-32chars!!";
    private SecretKey secretKey;

    @BeforeEach
    void setUp() {
        secretKey = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
        webTestClient = WebTestClient
                .bindToServer()
                .baseUrl("http://localhost:" + port)
                .build();
    }

    @Test
    @DisplayName("Request sin token a ruta protegida → 401")
    void givenNoToken_whenAccessingProtectedRoute_thenReturns401() {
        webTestClient.get()
                .uri("/api/v1/users/1")
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.UNAUTHORIZED)
                .expectBody()
                .jsonPath("$.error").isEqualTo("UNAUTHORIZED");
    }

    @Test
    @DisplayName("Request con token expirado → 401")
    void givenExpiredToken_whenAccessingProtectedRoute_thenReturns401() {
        String expiredToken = Jwts.builder()
                .subject("user-123")
                .expiration(Date.from(Instant.now().minusSeconds(3600)))
                .signWith(secretKey)
                .compact();

        webTestClient.get()
                .uri("/api/v1/users/1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + expiredToken)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    @DisplayName("Request con token malformado → 401")
    void givenMalformedToken_whenAccessingProtectedRoute_thenReturns401() {
        webTestClient.get()
                .uri("/api/v1/users/1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer not.a.valid.token")
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    @DisplayName("Ruta pública /auth/login no requiere token")
    void givenPublicPath_whenAccessing_thenDoesNotRequireToken() {
        // La ruta pública no requiere JWT (aunque el servicio downstream no exista,
        // el gateway no debe rechazar por JWT sino pasar la request)
        webTestClient.post()
                .uri("/api/v1/auth/login")
                .exchange()
                // El gateway no bloquea por JWT; puede dar 503 si el auth-service no está levantado
                .expectStatus().value(status ->
                        org.assertj.core.api.Assertions.assertThat(status)
                                .isNotEqualTo(HttpStatus.UNAUTHORIZED.value())
                                .isNotEqualTo(HttpStatus.FORBIDDEN.value())
                );
    }

    @Test
    @DisplayName("Header Authorization sin Bearer prefix → 401")
    void givenTokenWithoutBearerPrefix_whenAccessingProtectedRoute_thenReturns401() {
        String token = buildValidToken("user-123", List.of("ROLE_USER"));

        webTestClient.get()
                .uri("/api/v1/users/1")
                .header(HttpHeaders.AUTHORIZATION, token) // sin "Bearer "
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    // ── helpers ──────────────────────────────────────────────────────────────

    private String buildValidToken(String userId, List<String> roles) {
        return Jwts.builder()
                .subject(userId)
                .claim("roles", roles)
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plusSeconds(3600)))
                .signWith(secretKey)
                .compact();
    }
}
