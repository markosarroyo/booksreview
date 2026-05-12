package com.portfolio.gateway.routing;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

/**
 * Tests de integración de enrutamiento usando WireMock.
 *
 * WireMock simula los microservicios downstream, permitiendo verificar
 * que el gateway enruta correctamente sin necesidad de levantar los servicios reales.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class GatewayRoutingIntegrationTest {

    private static WireMockServer userServiceMock;
    private static WireMockServer productServiceMock;

    private static final String SECRET = "test-secret-key-for-testing-only-32chars!!";
    private static SecretKey secretKey;

    @Autowired
    private WebTestClient webTestClient;

    @BeforeAll
    static void startWireMock() {
        secretKey = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

        userServiceMock = new WireMockServer(WireMockConfiguration.options().dynamicPort());
        productServiceMock = new WireMockServer(WireMockConfiguration.options().dynamicPort());

        userServiceMock.start();
        productServiceMock.start();
    }

    @AfterAll
    static void stopWireMock() {
        userServiceMock.stop();
        productServiceMock.stop();
    }

    @BeforeEach
    void resetStubs() {
        userServiceMock.resetAll();
        productServiceMock.resetAll();
    }

    /**
     * Registra las URLs dinámicas de WireMock en el contexto de Spring
     * para que el gateway enrute hacia los mocks.
     */
    @DynamicPropertySource
    static void gatewayProperties(DynamicPropertyRegistry registry) {
        // Se sobreescriben después del inicio, por eso usamos suppliers
        registry.add("USER_SERVICE_URL", () -> "http://localhost:" + userServiceMock.port());
        registry.add("PRODUCT_SERVICE_URL", () -> "http://localhost:" + productServiceMock.port());
    }

    @Test
    @Order(1)
    @DisplayName("GET /api/v1/users/{id} → enruta al user-service y retorna 200")
    void givenValidToken_whenGetUser_thenRoutesToUserService() {
        // given
        userServiceMock.stubFor(get(urlEqualTo("/api/v1/users/42"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody("""
                                {"id": "42", "name": "Juan García", "email": "juan@example.com"}
                                """)));

        // when & then
        webTestClient.get()
                .uri("/api/v1/users/42")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + validToken())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo("42")
                .jsonPath("$.name").isEqualTo("Juan García");

        // Verificar que el gateway añadió el header X-Gateway-Source
        userServiceMock.verify(getRequestedFor(urlEqualTo("/api/v1/users/42"))
                .withHeader("X-Gateway-Source", equalTo("api-gateway"))
                .withHeader("X-Correlation-ID", matching(".+")));
    }

    @Test
    @Order(2)
    @DisplayName("El gateway propaga X-User-Id y X-User-Roles al microservicio")
    void givenValidToken_whenRouting_thenPropagatesUserHeaders() {
        // given
        userServiceMock.stubFor(get(urlEqualTo("/api/v1/users/me"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody("{\"status\": \"ok\"}")));

        // when
        webTestClient.get()
                .uri("/api/v1/users/me")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + validToken("user-99", List.of("ROLE_USER", "ROLE_ADMIN")))
                .exchange()
                .expectStatus().isOk();

        // then - downstream recibió la identidad del usuario
        userServiceMock.verify(getRequestedFor(urlEqualTo("/api/v1/users/me"))
                .withHeader("X-User-Id", equalTo("user-99"))
                .withHeader("X-User-Roles", containing("ROLE_USER")));
    }

    @Test
    @Order(3)
    @DisplayName("Servicio caído → circuit breaker activa fallback con 503")
    void givenDownstreamServiceDown_whenRequest_thenFallbackReturns503() {
        // El mock no tiene stub → conexión rechazada
        userServiceMock.stubFor(get(urlMatching("/api/v1/users/.*"))
                .willReturn(aResponse().withFault(
                        com.github.tomakehurst.wiremock.http.Fault.CONNECTION_RESET_BY_PEER)));

        webTestClient.get()
                .uri("/api/v1/users/1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + validToken())
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.SERVICE_UNAVAILABLE)
                .expectBody()
                .jsonPath("$.error").isEqualTo("SERVICE_UNAVAILABLE");
    }

    @Test
    @Order(4)
    @DisplayName("GET /api/v1/products → enruta al product-service correctamente")
    void givenValidToken_whenGetProducts_thenRoutesToProductService() {
        // given
        productServiceMock.stubFor(get(urlEqualTo("/api/v1/products"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody("[{\"id\": 1, \"name\": \"Laptop\"}]")));

        // when & then
        webTestClient.get()
                .uri("/api/v1/products")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + validToken())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].name").isEqualTo("Laptop");
    }

    // ── helpers ──────────────────────────────────────────────────────────────

    private String validToken() {
        return validToken("user-123", List.of("ROLE_USER"));
    }

    private String validToken(String userId, List<String> roles) {
        return Jwts.builder()
                .subject(userId)
                .claim("roles", roles)
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plusSeconds(3600)))
                .signWith(secretKey)
                .compact();
    }
}
