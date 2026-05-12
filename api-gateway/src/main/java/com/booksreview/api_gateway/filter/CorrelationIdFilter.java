package com.booksreview.api_gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Filtro global que asigna un ID de correlación único a cada request.
 *
 * - Si el cliente ya envía X-Correlation-ID, lo respetamos y propagamos
 * - Si no viene, generamos uno nuevo (UUID)
 * - Se añade al MDC para que aparezca en todos los logs de la request
 * - Se propaga como header hacia los microservicios downstream
 * - Se incluye en la response para que el cliente pueda trazarlo
 *
 * Orden: HIGHEST_PRECEDENCE → debe ser el primero en ejecutarse
 */
@Slf4j
@Component
public class CorrelationIdFilter implements GlobalFilter, Ordered {

    public static final String CORRELATION_ID_HEADER = "X-Correlation-ID";
    public static final String MDC_CORRELATION_KEY = "correlationId";
    public static final String MDC_USER_ID_KEY = "userId";

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String correlationId = exchange.getRequest().getHeaders().getFirst(CORRELATION_ID_HEADER);

        if (correlationId == null || correlationId.isBlank()) {
            correlationId = UUID.randomUUID().toString();
        }

        final String finalCorrelationId = correlationId;

        // Propagar como header hacia downstream
        ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                .header(CORRELATION_ID_HEADER, finalCorrelationId)
                .build();

        // Incluir en la response para trazabilidad del cliente
        exchange.getResponse().getHeaders().add(CORRELATION_ID_HEADER, finalCorrelationId);

        return chain.filter(exchange.mutate().request(mutatedRequest).build())
                .contextWrite(ctx -> {
                    MDC.put(MDC_CORRELATION_KEY, finalCorrelationId);
                    return ctx;
                })
                .doFinally(signalType -> {
                    // Limpiar MDC para evitar contaminación entre requests en el thread pool
                    MDC.remove(MDC_CORRELATION_KEY);
                    MDC.remove(MDC_USER_ID_KEY);
                });
    }
}
