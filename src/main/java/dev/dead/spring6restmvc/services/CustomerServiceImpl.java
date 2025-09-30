package dev.dead.spring6restmvc.services;

import dev.dead.spring6restmvc.models.CustomerDTO;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
public class CustomerServiceImpl implements CustomerService {
    private final Map<UUID, CustomerDTO> customers = new HashMap<>();

    public CustomerServiceImpl() {
        CustomerDTO customerDTO1 = CustomerDTO.builder()
                .id(UUID.randomUUID())
                .customerName("John")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .version(1)
                .build();
        CustomerDTO customerDTO2 = CustomerDTO.builder()
                .id(UUID.randomUUID())
                .customerName("Joe")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .version(1)
                .build();

        CustomerDTO customerDTO3 = CustomerDTO.builder()
                .id(UUID.randomUUID())
                .customerName("Biden")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .version(1)
                .build();


        customers.put(customerDTO1.getId(), customerDTO1);
        customers.put(customerDTO2.getId(), customerDTO2);
        customers.put(customerDTO3.getId(), customerDTO3);
    }


    @Override
    public Optional<CustomerDTO> getCustomerById(UUID id) {
        log.debug("Get customer by id - Service {}", id);
        return Optional.of(customers.get(id));

    }

    @Override
    public @NotNull List<CustomerDTO> getCustomers() {
        log.debug("Get customers - Service");
        return new ArrayList<>(customers.values());
    }

    @Override
    public @NotNull CustomerDTO saveNewCustomer(@NotNull CustomerDTO customerDTO) {
        CustomerDTO savedCustomerDTO = CustomerDTO.builder()
                .id(UUID.randomUUID())
                .version(1)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .customerName(customerDTO.getCustomerName())
                .build();
        customers.put(savedCustomerDTO.getId(), savedCustomerDTO);
        return savedCustomerDTO;


    }

    @Override
    public @NotNull CustomerDTO updateCustomerById(UUID customerId, @NotNull CustomerDTO customerDTO) {
        CustomerDTO existing = customers.get(customerId);
        existing.setCustomerName(customerDTO.getCustomerName());
        existing.setUpdatedAt(LocalDateTime.now());
        existing.setVersion(existing.getVersion() + 1);
        return existing;
        // redundant
//        return customers.replace(customerId, existing);
    }

    @Override
    public Boolean deleteCustomerById(UUID customerId) {
        log.debug("Starting customers count: {}", customers.size());
        log.debug("Delete customer by id - Service {}", customerId);
        customers.remove(customerId);
        log.debug("Remaining customers count: {}", customers.size());
        return true;

    }

    @Override
    public void patchCustomer(UUID customerId, @NotNull CustomerDTO customerDTO) {
        log.debug("Patching customer by id - Service {}", customerId);
        var existing = customers.get(customerId);

        if (StringUtils.hasText(customerDTO.getCustomerName())) {
            existing.setCustomerName(customerDTO.getCustomerName());
        }
        existing.setUpdatedAt(LocalDateTime.now());
        existing.setVersion(existing.getVersion() + 1);


    }
}
