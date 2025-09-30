package dev.dead.spring6restmvc.controllers;

import dev.dead.spring6restmvc.entities.Customer;
import dev.dead.spring6restmvc.mappers.CustomerMapper;
import dev.dead.spring6restmvc.models.CustomerDTO;
import dev.dead.spring6restmvc.repositories.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CustomerControllerIT {
    @Autowired
    CustomerController customerController;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    private CustomerMapper customerMapper;

    @Rollback
    @Transactional
    @Test
    void updateFoundCustomerById() {
        Customer customer = customerRepository.findAll()
                .get(0);
        CustomerDTO customerDto = customerMapper.customerToCustomerDTO(customer);
        customerDto.setCustomerName("New Test Customer");

        customerDto.setVersion(null);
        customerDto.setId(null);
        customerDto.setCreatedAt(null);
        customerDto.setUpdatedAt(null);

        ResponseEntity<CustomerDTO> responseEntity = customerController.updateCustomerById(customer.getId(), customerDto);
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());

        Customer updatedCustomer = customerRepository.getById(customer.getId());
        assertNotNull(updatedCustomer);
        assertEquals(customerDto.getCustomerName(), updatedCustomer.getCustomerName());
        assertEquals(1, updatedCustomer.getVersion());
        assertNotNull(updatedCustomer.getCreatedAt());
        assertNotNull(updatedCustomer.getUpdatedAt());

    }

    @Test
    void deleteNotFoundCustomerById() {
        assertThrows(NotFoundException.class, () -> customerController.deleteCustomerById(UUID.randomUUID()));

    }

    @Rollback
    @Transactional
    @Test
    void deleteFoundCustomerById() {
        Customer customer = customerRepository.findAll()
                .get(0);
        ResponseEntity responseEntity = customerController.deleteCustomerById(customer.getId());
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        assertTrue(customerRepository.findById(customer.getId())
                .isEmpty());
        assertThrows(NotFoundException.class, () -> customerController.getCustomerById(customer.getId()));

    }

    @Test
    void getCustomerByIdNotFound() {
        assertThrows(NotFoundException.class, () -> {
            customerController.getCustomerById(UUID.randomUUID());
        });
    }

    @Test
    void getCustomerByIdFound() {
        Customer customer = customerRepository.findAll()
                .get(0);
        CustomerDTO customerDTO = customerController.getCustomerById(customer.getId());

        assertNotNull(customerDTO);

        assertEquals(customer.getId(), customerDTO.getId());
        assertEquals(customer.getCustomerName(), customerDTO.getCustomerName());
    }

    @Test
    void getCustomers() {
        List<CustomerDTO> dtos = customerController.getCustomers();
        assertEquals(3, dtos.size());
    }

    @Rollback
    @Transactional
    @Test
    void getCustomersEmptyList() {
        customerRepository.deleteAll();
        customerRepository.flush();

        List<CustomerDTO> dtos = customerController.getCustomers();
        assertEquals(0, dtos.size());
    }

}