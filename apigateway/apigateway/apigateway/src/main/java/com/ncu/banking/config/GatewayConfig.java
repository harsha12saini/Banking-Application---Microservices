package com.ncu.banking.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
            .route("authservice", r -> r.path("/auth/**")
                .uri("http://localhost:3005"))
            .route("accountservice", r -> r.path("/accounts/**")
                .uri("http://localhost:3000"))
            .route("customerservice", r -> r.path("/customers/**")
                .uri("http://localhost:3001"))
            .route("transactionservice", r -> r.path("/transactions/**")
                .uri("http://localhost:3002"))
            .build();
    }
}
