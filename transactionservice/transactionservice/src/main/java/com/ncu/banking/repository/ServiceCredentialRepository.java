package com.ncu.banking.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ServiceCredentialRepository {

    private final JdbcTemplate jdbcTemplate;

    public ServiceCredentialRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Fetch username & password for Basic Auth for a given service.
     * Returns null if not found.
     */
    public ServiceCredential getCredential(String serviceName) {
        try {
            String sql = "SELECT username, password FROM service_credentials WHERE service_name = ?";
            ServiceCredential cred = jdbcTemplate.queryForObject(sql, (rs, rowNum) ->
                    new ServiceCredential(rs.getString("username"), rs.getString("password")),
                    serviceName
            );
            System.out.println("[ServiceCredentialRepository] Fetched credentials for: " + serviceName);
            return cred;
        } catch (Exception e) {
            System.out.println("[ServiceCredentialRepository] Credential not found for: " + serviceName);
            return null;
        }
    }

    /**
     * Fetch shared secret for API Gateway validation for a given service.
     * Returns null if not found.
     */
    public String getSharedSecret(String serviceName) {
        try {
            String sql = "SELECT shared_secret FROM service_credentials WHERE service_name = ?";
            String secret = jdbcTemplate.queryForObject(sql, String.class, serviceName);
            System.out.println("[ServiceCredentialRepository] Fetched shared secret for: " + serviceName);
            return secret;
        } catch (Exception e) {
            System.out.println("[ServiceCredentialRepository] Shared secret not found for: " + serviceName);
            return null;
        }
    }

    /** Simple DTO for storing service credentials */
    public static class ServiceCredential {
        private final String username;
        private final String password;

        public ServiceCredential(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public String getUsername() { return username; }
        public String getPassword() { return password; }
    }
}
