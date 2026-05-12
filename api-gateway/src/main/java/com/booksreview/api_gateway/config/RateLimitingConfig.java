package com.booksreview.api_gateway.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import reactor.core.publisher.Mono;

/**
 * Configuración de rate limiting.
 *
 * Estrategia:
 *  - Por defecto: límite por IP del cliente
 *  - Si hay JWT válido: límite por userId (más granular, penaliza al usuario no a la IP)
 *
 * Los valores de replenishRate y burstCapacity se configuran en application.yml
 * para mantener la configuración centralizada.
 */
@Configuration
public class RateLimitingConfig {

    private static final String HEADER_USER_ID = "X-User-Id";
    private static final String ANONYMOUS_PREFIX = "anon:";
    private static final String USER_PREFIX = "user:";

    /**
     * Resolver principal: usa userId si está autenticado, IP si no lo está.
     * Esto permite aplicar cuotas diferenciadas por tipo de cliente.
     */
    @Bean
    @Primary
    public KeyResolver userOrIpKeyResolver() {
        return exchange -> {
            String userId = exchange.getRequest().getHeaders().getFirst(HEADER_USER_ID);
            if (userId != null && !userId.isBlank()) {
                return Mono.just(USER_PREFIX + userId);
            }
            return ipKeyResolver().resolve(exchange);
        };
    }

    /**
     * Resolver por IP para rutas públicas o fallback.
     * Referenciado en application.yml como #{@ipKeyResolver}
     */
    @Bean
    public KeyResolver ipKeyResolver() {
        return exchange -> {
            // Considera X-Forwarded-For cuando hay proxies/load balancers delante
            String forwardedFor = exchange.getRequest().getHeaders().getFirst("X-Forwarded-For");
            if (forwardedFor != null && !forwardedFor.isBlank()) {
                // Tomar solo la primera IP de la cadena de proxies
                String clientIp = forwardedFor.split(",")[0].trim();
                return Mono.just(ANONYMOUS_PREFIX + clientIp);
            }

            String remoteAddress = exchange.getRequest().getRemoteAddress() != null
                    ? exchange.getRequest().getRemoteAddress().getAddress().getHostAddress()
                    : "unknown";

            return Mono.just(ANONYMOUS_PREFIX + remoteAddress);
        };
    }
}
