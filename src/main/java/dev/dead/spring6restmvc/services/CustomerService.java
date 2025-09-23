package dev.dead.spring6restmvc.services;

import dev.dead.spring6restmvc.models.Customer;

import java.util.List;
import java.util.UUID;

public interface CustomerService {

    Customer getCustomerById(UUID id);

    List<Customer> getCustomers();

    Customer saveNewCustomer(Customer customer);
}
