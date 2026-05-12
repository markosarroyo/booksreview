package com.booksreview.api_gateway.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Map;

/**
 * Controlador de fallback para circuit breakers.
 *
 * Cuando Resilience4j abre el circuito, redirige aquí en lugar de
 * dejar al cliente esperando un timeout. Devuelve una respuesta
 * degradada pero informativa.
 */
@Slf4j
@RestController
@RequestMapping("/fallback")
public class FallbackController {

    @GetMapping("/user-service")
    public Mono<ResponseEntity<Map<String, Object>>> userServiceFallback(ServerWebExchange exchange) {
        return buildFallbackResponse("user-service", exchange);
    }

    @GetMapping("/book-service")
    public Mono<ResponseEntity<Map<String, Object>>> productServiceFallback(ServerWebExchange exchange) {
        return buildFallbackResponse("book-service", exchange);
    }

    @GetMapping("/review-service")
    public Mono<ResponseEntity<Map<String, Object>>> orderServiceFallback(ServerWebExchange exchange) {
        return buildFallbackResponse("review-service", exchange);
    }

    private Mono<ResponseEntity<Map<String, Object>>> buildFallbackResponse(
            String serviceName, ServerWebExchange exchange) {

        String correlationId = exchange.getRequest().getHeaders()
                .getFirst("X-Correlation-ID");

        log.warn("Circuit breaker activado para servicio: {} | correlationId: {}",
                serviceName, correlationId);

        Map<String, Object> body = Map.of(
                "error", "SERVICE_UNAVAILABLE",
                "message", "El servicio " + serviceName + " no está disponible temporalmente. Inténtelo de nuevo en unos segundos.",
                "service", serviceName,
                "timestamp", Instant.now().toString(),
                "correlationId", correlationId != null ? correlationId : "unknown"
        );

        return Mono.just(ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(body));
    }
}
