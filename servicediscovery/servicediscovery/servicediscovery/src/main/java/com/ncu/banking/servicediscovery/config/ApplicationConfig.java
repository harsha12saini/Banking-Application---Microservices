package com.ncu.banking.servicediscovery.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class ApplicationConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/eureka/**").permitAll() // allow clients to register
                .anyRequest().authenticated()
            )
            .httpBasic(httpBasic -> {})
            .cors(cors -> {});

        return http.build();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails user = User.withUsername("discoveryservice")
                               .password("{noop}discoveryservice")
                               .roles("USER")
                               .build();
        return new InMemoryUserDetailsManager(user);
    }
}

/*import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class ApplicationConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception
    {

        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
            .requestMatchers("/eureka/**").authenticated()
            .anyRequest().permitAll()
            )
            .httpBasic(httpBasic -> {})
            .cors(cors -> {});

            return http.build();
    }
} */