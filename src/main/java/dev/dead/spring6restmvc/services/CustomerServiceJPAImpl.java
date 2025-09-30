package dev.dead.spring6restmvc.services;

import dev.dead.spring6restmvc.mappers.CustomerMapper;
import dev.dead.spring6restmvc.models.CustomerDTO;
import dev.dead.spring6restmvc.repositories.BeerRepository;
import dev.dead.spring6restmvc.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@Primary
@RequiredArgsConstructor
public class CustomerServiceJPAImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final BeerRepository beerRepository;

    @Override
    public Optional<CustomerDTO> getCustomerById(UUID id) {
        return Optional.ofNullable(customerMapper.customerToCustomerDTO(customerRepository.findById(id)
                .orElse(null)));
    }

    @Override
    public List<CustomerDTO> getCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(customerMapper::customerToCustomerDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CustomerDTO saveNewCustomer(CustomerDTO customerDTO) {
        return null;
    }

    @Override
    public Optional<CustomerDTO> updateCustomerById(UUID customerId, CustomerDTO customerDTO) {
        return Optional.empty();

    }

    @Override
    public Boolean deleteCustomerById(UUID customerId) {
        if (customerRepository.existsById(customerId)) {
            customerRepository.deleteById(customerId);
            customerRepository.flush();
            return true;
        }
        return false;

    }

    @Override
    public Optional<CustomerDTO> patchCustomer(UUID customerId, CustomerDTO customerDTO) {
        return Optional.empty();

    }
}
