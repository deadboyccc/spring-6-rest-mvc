package dev.dead.spring6restmvc.services;

import dev.dead.spring6restmvc.models.CustomerDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerService {

    Optional<CustomerDTO> getCustomerById(UUID id);

    List<CustomerDTO> getCustomers();

    CustomerDTO saveNewCustomer(CustomerDTO customerDTO);

    CustomerDTO updateCustomerById(UUID customerId, CustomerDTO customerDTO);

    void deleteCustomerById(UUID customerId);

    void patchCustomer(UUID customerId, CustomerDTO customerDTO);
}
