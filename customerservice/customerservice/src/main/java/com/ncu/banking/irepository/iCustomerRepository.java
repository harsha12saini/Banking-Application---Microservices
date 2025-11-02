package com.ncu.banking.irepository;

import com.ncu.banking.model.Customer;
import java.util.List;

public interface iCustomerRepository {
    List<Customer> GetAllCustomers();
    Customer GetCustomerById(String customerId);
    Customer GetCustomerByEmail(String email);
    Customer AddCustomer(Customer customer);
    Customer UpdateCustomer(Customer customer);
    boolean DeleteCustomer(String customerId);
    Integer GetCustomerCount();
    List<Customer> GetCustomersByPage(int page, int size);

}
