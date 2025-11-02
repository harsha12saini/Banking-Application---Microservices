package com.ncu.banking.controller;

import com.ncu.banking.dto.AccountDto;
import com.ncu.banking.dto.TransactionDto;
import com.ncu.banking.model.Customer;
import com.ncu.banking.model.ServiceCredential;
import com.ncu.banking.service.CustomerService;
import com.ncu.banking.repository.ServiceCredentialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping(value = "/customers", produces = "application/json")
public class CustomerController {

    private final CustomerService customerService;
    private final ServiceCredentialRepository serviceCredentialRepository;
    private final RestTemplate restTemplate;

    @Autowired
    public CustomerController(CustomerService customerService,
                              ServiceCredentialRepository serviceCredentialRepository,
                              RestTemplate restTemplate) {
        this.customerService = customerService;
        this.serviceCredentialRepository = serviceCredentialRepository;
        this.restTemplate = restTemplate;
    }

    @GetMapping("/")
    public ResponseEntity<List<Customer>> getAllCustomers() {
        List<Customer> customers = customerService.getAllCustomers();
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable String customerId) {
        Customer customer = customerService.getCustomerById(customerId);
        return ResponseEntity.ok(customer);
    }

    @PostMapping("/")
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
        Customer created = customerService.addCustomer(customer);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{customerId}")
    public ResponseEntity<String> updateCustomer(@PathVariable String customerId,
                                                 @RequestBody Customer updatedCustomer) {
        boolean updated = customerService.updateCustomer(customerId, updatedCustomer);
        if (!updated) {
            throw new RuntimeException("Customer not found: " + customerId);
        }
        return ResponseEntity.ok("Customer updated successfully");
    }

    @DeleteMapping("/{customerId}")
    public ResponseEntity<String> deleteCustomer(@PathVariable String customerId) {
        boolean deleted = customerService.deleteCustomer(customerId);
        if (!deleted) {
            throw new RuntimeException("Customer not found: " + customerId);
        }
        return ResponseEntity.ok("Customer deleted successfully");
    }

    @GetMapping("/{customerId}/details")
    public ResponseEntity<List<AccountDto>> getCustomerAccounts(@PathVariable String customerId) {

        ServiceCredential accountCred = new ServiceCredential();
        accountCred.setServiceName("accountservice");
        accountCred.setSharedSecret(serviceCredentialRepository.getSharedSecret("accountservice"));

        String accountAuth = Base64.getEncoder()
                .encodeToString((accountCred.getServiceName() + ":" + accountCred.getSharedSecret()).getBytes());

        HttpHeaders accountHeaders = new HttpHeaders();
        accountHeaders.set("Authorization", "Basic " + accountAuth);
        accountHeaders.setAccept(List.of(MediaType.APPLICATION_JSON));
        HttpEntity<String> accountEntity = new HttpEntity<>(accountHeaders);

        ResponseEntity<List<AccountDto>> accountsResponse = restTemplate.exchange(
                "http://localhost:3000/accounts/customers/" + customerId + "/accounts",
                HttpMethod.GET,
                accountEntity,
                new ParameterizedTypeReference<List<AccountDto>>() {}
        );
        List<AccountDto> accounts = accountsResponse.getBody();

        if (accounts != null) {
            for (AccountDto account : accounts) {
                ServiceCredential txnCred = new ServiceCredential();
                txnCred.setServiceName("transactionservice");
                txnCred.setSharedSecret(serviceCredentialRepository.getSharedSecret("transactionservice"));

                String txnAuth = Base64.getEncoder()
                        .encodeToString((txnCred.getServiceName() + ":" + txnCred.getSharedSecret()).getBytes());

                HttpHeaders txnHeaders = new HttpHeaders();
                txnHeaders.set("Authorization", "Basic " + txnAuth);
                txnHeaders.setAccept(List.of(MediaType.APPLICATION_JSON));
                HttpEntity<String> txnEntity = new HttpEntity<>(txnHeaders);

                ResponseEntity<List<TransactionDto>> txnResponse = restTemplate.exchange(
                        "http://localhost:3002/transactions/account/" + account.getAccountId(),
                        HttpMethod.GET,
                        txnEntity,
                        new ParameterizedTypeReference<List<TransactionDto>>() {}
                );
                account.setTransactions(txnResponse.getBody());
            }
        }

        return ResponseEntity.ok(accounts != null ? accounts : Collections.emptyList());
    }
}
