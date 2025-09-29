package dev.dead.spring6restmvc.mappers;

import dev.dead.spring6restmvc.entities.Customer;
import dev.dead.spring6restmvc.models.CustomerDTO;
import org.mapstruct.Mapper;

@Mapper
public interface CustomerMapper {
    Customer customerDTOToCustomer(CustomerDTO customerDTO);

    CustomerDTO customerToCustomerDTO(Customer customer);
}
