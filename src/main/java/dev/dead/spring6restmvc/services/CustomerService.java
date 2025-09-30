package dev.dead.spring6restmvc.services;

import dev.dead.spring6restmvc.models.CustomerDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerService {

    Optional<CustomerDTO> getCustomerById(UUID id);

    List<CustomerDTO> getCustomers();

    CustomerDTO saveNewCustomer(CustomerDTO customerDTO);

    Optional<CustomerDTO> updateCustomerById(UUID customerId, CustomerDTO customerDTO);

    Boolean deleteCustomerById(UUID customerId);

    Optional<CustomerDTO> patchCustomer(UUID customerId, CustomerDTO customerDTO);
}
