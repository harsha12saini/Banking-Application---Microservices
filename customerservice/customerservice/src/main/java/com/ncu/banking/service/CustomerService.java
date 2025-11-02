package com.ncu.banking.service;

import com.ncu.banking.dto.AccountDto;
import com.ncu.banking.exception.CustomerNotFoundException;
import com.ncu.banking.exception.DatabaseException;
import com.ncu.banking.model.Customer;
import com.ncu.banking.irepository.iCustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Service
public class CustomerService {

    private final iCustomerRepository customerRepository;
    private final RestTemplate restTemplate;

    @Autowired
    public CustomerService(iCustomerRepository customerRepository, RestTemplate restTemplate) {
        this.customerRepository = customerRepository;
        this.restTemplate = restTemplate;
    }

    // ---------------- Basic CRUD ----------------
    public List<Customer> getAllCustomers() {
        try {
            return customerRepository.GetAllCustomers();
        } catch (Exception e) {
            throw new DatabaseException("Failed to fetch customers", e);
        }
    }

    public Customer getCustomerById(String customerId) {
        try {
            Customer customer = customerRepository.GetCustomerById(customerId);
            if (customer == null) {
                throw new CustomerNotFoundException("Customer ID " + customerId + " not found");
            }
            return customer;
        } catch (CustomerNotFoundException ex) {
            throw ex;
        } catch (Exception e) {
            throw new DatabaseException("Failed to fetch customer by ID", e);
        }
    }

    public Customer addCustomer(Customer customer) {
        try {
            return customerRepository.AddCustomer(customer);
        } catch (Exception e) {
            throw new DatabaseException("Failed to add customer", e);
        }
    }

    public boolean updateCustomer(String customerId, Customer updatedCustomer) {
        try {
            Customer existing = customerRepository.GetCustomerById(customerId);
            if (existing == null) {
                throw new CustomerNotFoundException("Customer ID " + customerId + " not found");
            }
            existing.setName(updatedCustomer.getName());
            existing.setEmail(updatedCustomer.getEmail());
            existing.setPhone(updatedCustomer.getPhone());
            customerRepository.UpdateCustomer(existing);
            return true;
        } catch (CustomerNotFoundException ex) {
            throw ex;
        } catch (Exception e) {
            throw new DatabaseException("Failed to update customer", e);
        }
    }

    public boolean deleteCustomer(String customerId) {
        try {
            boolean deleted = customerRepository.DeleteCustomer(customerId);
            if (!deleted) {
                throw new CustomerNotFoundException("Customer ID " + customerId + " not found");
            }
            return true;
        } catch (CustomerNotFoundException ex) {
            throw ex;
        } catch (Exception e) {
            throw new DatabaseException("Failed to delete customer", e);
        }
    }

    // ---------------- Fetch Customer + Accounts ----------------
    public CustomerWithAccounts getCustomerAccountDetails(String customerId) {
        try {
            Customer customer = getCustomerById(customerId); // will throw if not found

            ResponseEntity<List<AccountDto>> response = restTemplate.exchange(
                    "http://localhost:3001/customers/{customerId}/accounts",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<AccountDto>>() {},
                    customerId
            );

            List<AccountDto> accounts = response.getBody();
            CustomerWithAccounts wrapper = new CustomerWithAccounts();
            wrapper.setCustomer(customer);
            wrapper.setAccounts(accounts != null ? accounts : Collections.emptyList());
            return wrapper;

        } catch (CustomerNotFoundException ex) {
            throw ex;
        } catch (Exception e) {
            throw new DatabaseException("Failed to fetch customer accounts", e);
        }
    }

    // ---------------- DTO Wrapper ----------------
    public static class CustomerWithAccounts {
        private Customer customer;
        private List<AccountDto> accounts;

        public Customer getCustomer() { return customer; }
        public void setCustomer(Customer customer) { this.customer = customer; }

        public List<AccountDto> getAccounts() { return accounts; }
        public void setAccounts(List<AccountDto> accounts) { this.accounts = accounts; }
    }
}
