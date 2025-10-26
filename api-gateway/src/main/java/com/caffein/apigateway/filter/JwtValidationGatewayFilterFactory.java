package com.caffein.apigateway.filter;

import com.caffein.apigateway.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtValidationGatewayFilterFactory extends AbstractGatewayFilterFactory<Object> {

    private final JwtService jwtService;

    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
            String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                log.warn("Missing or invalid Authorization header");
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            String token = authHeader.substring(7);

            try {
                if (!jwtService.isTokenValid(token)) {
                    log.warn("Invalid JWT token");
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                }

                String username = jwtService.extractUsername(token);
                log.debug("Valid token for user: {}", username);

                // Add username to request headers for downstream services
                exchange = exchange.mutate()
                        .request(r -> r.header("X-Auth-User", username))
                        .build();

                return chain.filter(exchange);

            } catch (Exception e) {
                log.error("JWT validation error: {}", e.getMessage());
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
        };
    }
}