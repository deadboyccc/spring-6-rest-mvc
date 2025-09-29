package dev.dead.spring6restmvc.repositories;

import dev.dead.spring6restmvc.entities.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomerRepositoryTest {
    @Autowired
    CustomerRepository customerRepository;

    @Test
    void testSaveCustomer() {
        Customer customer = customerRepository.save(Customer.builder()
                .customerName("Test Customer")
                .build());
        assertNotNull(customer);
        assertNotNull(customer.getId());

    }

}