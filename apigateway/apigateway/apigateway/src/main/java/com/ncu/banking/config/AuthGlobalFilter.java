package com.ncu.banking.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class AuthGlobalFilter implements GlobalFilter, Ordered {

    private final AuthHeaderFactory authFactory;

    @Autowired
    public AuthGlobalFilter(AuthHeaderFactory authFactory) {
        this.authFactory = authFactory;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String path = exchange.getRequest().getPath().value();
        System.out.println("[AuthGlobalFilter] Incoming request path: " + path);

        // Skip authentication for /auth/** endpoints
        if (path.startsWith("/auth/")) return chain.filter(exchange);

        // Determine target service from route
        Route route = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
        String serviceName = (route != null) ? route.getId() : "default";
        System.out.println("[AuthGlobalFilter] Target service: " + serviceName);

        // Inject service credentials
        String serviceSecret = authFactory.getServiceSecret(serviceName);
        String basicAuth = authFactory.buildAuthHeader(serviceName);

        System.out.println("[AuthGlobalFilter] Injecting headers -> "
                + "X-SERVICE-NAME: " + serviceName
                + ", X-SERVICE-SECRET: " + serviceSecret
                + ", Authorization: " + basicAuth);

        ServerHttpRequest mutatedRequest = exchange.getRequest()
                .mutate()
                .header("X-SERVICE-NAME", serviceName)
                .header("X-SERVICE-SECRET", serviceSecret)
                .header("Authorization", basicAuth)
                .build();

        return chain.filter(exchange.mutate().request(mutatedRequest).build());
    }

    @Override
    public int getOrder() {
        return -1; // High priority
    }
}
