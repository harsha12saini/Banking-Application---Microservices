package com.ncu.banking.repository;

import com.ncu.banking.exception.DatabaseException;
import com.ncu.banking.model.Customer;
import com.ncu.banking.irepository.iCustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CustomerRepositoryImpl implements iCustomerRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CustomerRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Customer> GetAllCustomers() {
        try {
            return jdbcTemplate.query("SELECT * FROM customer", new CustomerRowMapper());
        } catch (DataAccessException e) {
            throw new DatabaseException("Failed to fetch all customers", e);
        }
    }

    @Override
    public Customer GetCustomerById(String customerId) {
        try {
            return jdbcTemplate.queryForObject(
                    "SELECT * FROM customer WHERE customer_id = ?",
                    new CustomerRowMapper(),
                    customerId
            );
        } catch (DataAccessException e) {
            throw new DatabaseException("Failed to fetch customer by ID: " + customerId, e);
        }
    }

    @Override
    public Customer GetCustomerByEmail(String email) {
        try {
            return jdbcTemplate.queryForObject(
                    "SELECT * FROM customer WHERE LOWER(email) = ?",
                    new CustomerRowMapper(),
                    email.toLowerCase()
            );
        } catch (DataAccessException e) {
            throw new DatabaseException("Failed to fetch customer by email: " + email, e);
        }
    }

    @Override
    public Customer AddCustomer(Customer customer) {
        try {
            String customerId = "C" + (GetCustomerCount() + 101);
            customer.setCustomerId(customerId);

            String sql = "INSERT INTO customer (customer_id, name, email, phone) VALUES (?, ?, ?, ?)";
            jdbcTemplate.update(sql,
                    customer.getCustomerId(),
                    customer.getName(),
                    customer.getEmail(),
                    customer.getPhone()
            );

            return customer;
        } catch (DataAccessException e) {
            throw new DatabaseException("Failed to add customer: " + customer.getName(), e);
        }
    }

    @Override
    public Customer UpdateCustomer(Customer customer) {
        try {
            String sql = "UPDATE customer SET name = ?, email = ?, phone = ? WHERE customer_id = ?";
            int rows = jdbcTemplate.update(sql,
                    customer.getName(),
                    customer.getEmail(),
                    customer.getPhone(),
                    customer.getCustomerId()
            );
            if (rows == 0) {
                throw new DatabaseException("No customer found with ID: " + customer.getCustomerId());
            }
            return customer;
        } catch (DataAccessException e) {
            throw new DatabaseException("Failed to update customer: " + customer.getCustomerId(), e);
        }
    }

    @Override
    public boolean DeleteCustomer(String customerId) {
        try {
            int rows = jdbcTemplate.update("DELETE FROM customer WHERE customer_id = ?", customerId);
            return rows > 0;
        } catch (DataAccessException e) {
            throw new DatabaseException("Failed to delete customer: " + customerId, e);
        }
    }

    @Override
    public List<Customer> GetCustomersByPage(int page, int size) {
        try {
            int offset = page * size;
            return jdbcTemplate.query(
                    "SELECT * FROM customer LIMIT ? OFFSET ?",
                    new CustomerRowMapper(),
                    size, offset
            );
        } catch (DataAccessException e) {
            throw new DatabaseException("Failed to fetch paginated customers", e);
        }
    }

    @Override
    public Integer GetCustomerCount() {
        try {
            return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM customer", Integer.class);
        } catch (DataAccessException e) {
            throw new DatabaseException("Failed to count customers", e);
        }
    }
}
