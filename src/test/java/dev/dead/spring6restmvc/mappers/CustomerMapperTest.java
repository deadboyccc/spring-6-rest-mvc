package dev.dead.spring6restmvc.mappers;

import dev.dead.spring6restmvc.entities.Customer;
import dev.dead.spring6restmvc.models.CustomerDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = CustomerMapper.class)
class CustomerMapperTest {
    @Autowired
    CustomerMapper customerMapper;

    @Test
    void fromCustomerToCustomerDTO() {
        Customer customer = Customer.builder()
                .customerName("test")
                .build();

        CustomerDTO customerDTO = customerMapper.customerToCustomerDTO(customer);
        assertNotNull(customerDTO);
        assertEquals(customer.getCustomerName(), customerDTO.getCustomerName());
        assertInstanceOf(Customer.class, customer);
        assertInstanceOf(CustomerDTO.class, customerDTO);

    }

}