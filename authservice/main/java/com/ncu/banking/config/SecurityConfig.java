package com.ncu.banking.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
http
.csrf(csrf -> csrf.disable()) // Disable CSRF for APIs
.authorizeHttpRequests(auth -> auth
.requestMatchers("/auth/signup", "/auth/authenticate").permitAll() // Public endpoints
.anyRequest().authenticated() // Everything else requires authentication
)
.formLogin(form -> form.disable()) // Disable default login form
.httpBasic(httpBasic -> httpBasic.disable()); // Disable default HTTP Basic

return http.build();
}
}