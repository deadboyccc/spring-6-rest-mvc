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
        log.debug("Get customer by id - Service {}", id);
        return customers.get(id);

    }

    @Override
    public List<Customer> getCustomers() {
        log.debug("Get customers - Service");
        return new ArrayList<>(customers.values());
    }

    @Override
    public Customer saveNewCustomer(Customer customer) {
        Customer savedCustomer = Customer.builder()
                .id(UUID.randomUUID())
                .version(1)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .customerName(customer.getCustomerName())
                .build();
        customers.put(savedCustomer.getId(), savedCustomer);
        return savedCustomer;


    }

    @Override
    public Customer updateCustomerById(UUID customerId, Customer customer) {
        Customer existing = customers.get(customerId);
        existing.setCustomerName(customer.getCustomerName());
        existing.setUpdatedAt(LocalDateTime.now());
        existing.setVersion(existing.getVersion() + 1);
        return existing;
        // redundant
//        return customers.replace(customerId, existing);
    }
}
