package com.ncu.banking.security;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ServiceHeaderInterceptor implements ClientHttpRequestInterceptor {

    private static final String SERVICE_NAME = "accountservice";
    private static final String SERVICE_SECRET = "QzRrS3dGd0tsZ0l1U1B0Y3hRV2RrM1I5bENvaHhLMXhJb1BYZkZQbHRMSWg=";

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
            throws IOException {

        // Add custom headers for inter-service authentication
        request.getHeaders().add("X-SERVICE-NAME", SERVICE_NAME);
        request.getHeaders().add("X-SERVICE-SECRET", SERVICE_SECRET);

        System.out.println("[ServiceHeaderInterceptor] Added X-SERVICE-NAME and X-SERVICE-SECRET headers");
        return execution.execute(request, body);
    }
}
