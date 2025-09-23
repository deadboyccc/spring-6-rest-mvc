package dev.dead.spring6restmvc.services;

import dev.dead.spring6restmvc.models.Customer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
public class CustomerServiceImpl implements CustomerService {
    private Map<UUID, Customer> customers = new HashMap<>();

    public CustomerServiceImpl() {
        Customer customer1 = Customer.builder()
                .id(UUID.randomUUID())
                .customerName("John")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .version(1)
                .build();
        Customer customer2 = Customer.builder()
                .id(UUID.randomUUID())
                .customerName("Joe")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .version(1)
                .build();

        Customer customer3 = Customer.builder()
                .id(UUID.randomUUID())
                .customerName("Biden")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .version(1)
                .build();



        customers.put(customer1.getId(), customer1);
        customers.put(customer2.getId(), customer2);
        customers.put(customer3.getId(), customer3);
    }


    @Override
    public Customer getCustomerById(UUID id) {
        log.info("Get customer by id - Service {}", id);
        return customers.get(id);

    }

    @Override
    public List<Customer> getCustomers() {
        log.info("Get customers - Service");
        return new ArrayList<>(customers.values());
    }
}
