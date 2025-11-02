package com.ncu.banking.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;
import com.ncu.banking.repository.ServiceCredentialRepository;
import org.springframework.beans.factory.annotation.Autowired;


@Configuration
public class RestTemplateConfig {

    @Autowired
    private ServiceCredentialRepository credentialRepository;

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();

        restTemplate.getInterceptors().add((request, body, execution) -> {
    HttpHeaders headers = request.getHeaders();
    String uri = request.getURI().toString();

    if (uri.contains("/accounts")) {
        headers.set("X-SERVICE-NAME", "accountservice");
        headers.set("X-SERVICE-SECRET", credentialRepository.getSharedSecret("accountservice"));
        System.out.println("Adding headers for accountservice");
    } else if (uri.contains("/transactions")) {
        headers.set("X-SERVICE-NAME", "transactionservice");
        headers.set("X-SERVICE-SECRET", credentialRepository.getSharedSecret("transactionservice"));
        System.out.println("Adding headers for transactionservice");
    } else if (uri.contains("/customers")) {
        headers.set("X-SERVICE-NAME", "customerservice");
        headers.set("X-SERVICE-SECRET", credentialRepository.getSharedSecret("customerservice"));
        System.out.println("Adding headers for customerservice");
    }

    return execution.execute(request, body);
});


        return restTemplate;
    }
}
