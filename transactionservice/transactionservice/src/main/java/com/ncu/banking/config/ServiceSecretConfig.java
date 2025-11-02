package com.ncu.banking.config;

import com.ncu.banking.repository.ServiceCredentialRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.WebFilter;
import reactor.core.publisher.Mono;

@Configuration
public class ServiceSecretConfig {

    @Bean
    public WebFilter serviceSecretFilter(ServiceCredentialRepository credentialRepository) {
        return (exchange, chain) -> {
            var request = exchange.getRequest();
            String serviceName = request.getHeaders().getFirst("X-SERVICE-NAME");
            String serviceSecret = request.getHeaders().getFirst("X-SERVICE-SECRET");

            if (serviceName == null || serviceSecret == null) {
                exchange.getResponse().setStatusCode(org.springframework.http.HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            String expectedSecret = credentialRepository.getSharedSecret(serviceName);
            if (expectedSecret == null || !expectedSecret.trim().equals(serviceSecret.trim())) {
                exchange.getResponse().setStatusCode(org.springframework.http.HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(serviceName, null, null)
            );

            return chain.filter(exchange);
        };
    }
}
