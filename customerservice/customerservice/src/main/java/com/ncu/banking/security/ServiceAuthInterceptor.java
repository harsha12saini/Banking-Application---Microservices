package com.ncu.banking.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import com.ncu.banking.repository.ServiceCredentialRepository;

@Component
public class ServiceAuthInterceptor implements HandlerInterceptor {

    private final ServiceCredentialRepository repo;

    public ServiceAuthInterceptor(ServiceCredentialRepository repo) {
        this.repo = repo;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        String serviceName = request.getHeader("X-SERVICE-NAME");
        String serviceSecret = request.getHeader("X-SERVICE-SECRET");

        // Allow unauthenticated access for /auth/** routes
        if (request.getRequestURI().startsWith("/auth/")) {
            return true;
        }

        // Validate headers
        if (serviceName == null || serviceSecret == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing service authentication headers");
            return false;
        }

        String validSecret = repo.getSharedSecret(serviceName);
        if (validSecret == null || !validSecret.equals(serviceSecret)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid service credentials");
            return false;
        }

        System.out.println("[ServiceAuthInterceptor] Verified request from service: " + serviceName);
        return true;
    }
}
