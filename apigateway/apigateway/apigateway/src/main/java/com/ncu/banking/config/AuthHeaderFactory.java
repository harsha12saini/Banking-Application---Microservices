package com.ncu.banking.config;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import org.springframework.stereotype.Component;
import com.ncu.banking.repository.ServiceCredentialRepository;

@Component
public class AuthHeaderFactory {

    private final ServiceCredentialRepository repo;

    public AuthHeaderFactory(ServiceCredentialRepository repo) {
        this.repo = repo;
    }

    /** Builds Basic Auth header for the target service */
    public String buildAuthHeader(String serviceName) {
        ServiceCredentialRepository.ServiceCredential cred = repo.getCredential(serviceName);
        if (cred == null) return "";
        String auth = cred.getUsername() + ":" + cred.getPassword();
        return "Basic " + Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
    }

    /** Returns API Gateway secret for target service */
    public String getServiceSecret(String serviceName) {
        String secret = repo.getSharedSecret(serviceName);
        return (secret != null) ? secret : "";
    }
}
