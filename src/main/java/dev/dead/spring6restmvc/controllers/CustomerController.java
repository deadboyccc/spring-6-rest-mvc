package dev.dead.spring6restmvc.controllers;

import dev.dead.spring6restmvc.models.Customer;
import dev.dead.spring6restmvc.services.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
public class CustomerController {
    public static final String CUSTOMER_BASE_URL = "/api/v1/customer";
    public static final String CUSTOMER_ID_URL = CUSTOMER_BASE_URL + "/{customerId}";
    private final @NotNull CustomerService customerService;

    @PatchMapping(CUSTOMER_ID_URL)
    public @NotNull ResponseEntity<Customer> patchCustomer(@PathVariable("customerId") UUID customerId,
                                                           @RequestBody Customer customer) {
        log.debug("Patching customer with id - Controller {}", customerId);
        customerService.patchCustomer(customerId, customer);
        return ResponseEntity.noContent()
                .build();
    }

    @DeleteMapping(CUSTOMER_ID_URL)
    public @NotNull ResponseEntity deleteCustomer(@PathVariable("customerId") UUID customerId) {
        log.debug("Delete customer by id - Controller {}", customerId);
        customerService.deleteCustomerById(customerId);
        return ResponseEntity.noContent()
                .build();
    }

    @PutMapping(CUSTOMER_ID_URL)
    public @NotNull ResponseEntity<Customer> updateBeer(@PathVariable("customerId") @NotNull UUID customerId,
                                                        @RequestBody Customer customer) {
        customerService.updateCustomerById(customerId, customer);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add
                ("Location", "/api/v1/customer/" + customerId);
        return ResponseEntity
                .noContent()
                .headers(responseHeaders)
                .build();
    }

    @GetMapping(CUSTOMER_BASE_URL)
    public List<Customer> getCustomers() {
        log.debug("Get customers - Controller");
        return customerService.getCustomers();
    }

    @GetMapping(CUSTOMER_ID_URL)
    public Customer getCustomer(@PathVariable("customerId") UUID customerId) {
        log.debug("Get customer by id - Controller: {}", customerId);
        return customerService.getCustomerById(customerId);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @PostMapping(CUSTOMER_BASE_URL)
    public @NotNull ResponseEntity createCustomer(@RequestBody @NotNull Customer customer) {
        log.debug("Create customer - Controller: {}", customer.getCustomerName());
        Customer savedCustomer = customerService.saveNewCustomer(customer);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.LOCATION, "/api/v1/customer/" + savedCustomer.getId());
        return new ResponseEntity(headers, HttpStatus.CREATED);

    }

}
