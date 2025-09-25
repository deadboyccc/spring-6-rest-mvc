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
@RestController
@RequestMapping("/api/v1/customer")
@RequiredArgsConstructor
public class CustomerController {
    private final @NotNull CustomerService customerService;

    @PatchMapping("{customerId}")
    public @NotNull ResponseEntity<Customer> patchCustomer(@PathVariable("customerId") UUID customerId, @RequestBody Customer customer) {
        log.debug("Patching customer with id - Controller {}", customerId);
        customerService.patchCustomer(customerId, customer);
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("{customerId}")
    public @NotNull ResponseEntity deleteCustomer(@PathVariable("customerId") UUID customerId) {
        log.debug("Delete customer by id - Controller {}", customerId);
        customerService.deleteCustomerById(customerId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("{customerId}")
    public @NotNull ResponseEntity<Customer> updateBeer(@PathVariable("customerId") @NotNull UUID customerId, @RequestBody Customer customer) {
        customerService.updateCustomerById(customerId, customer);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add
                ("Location", "/api/v1/customer/" + customerId);
        return ResponseEntity
                .noContent()
                .headers(responseHeaders)
                .build();
    }
    @GetMapping
    public List<Customer> getCustomers() {
        log.debug("Get customers - Controller");
        return customerService.getCustomers();
    }

    @GetMapping("{customerId}")
    public Customer getCustomer(@PathVariable("customerId") UUID customerId) {
        log.debug("Get customer by id - Controller: {}", customerId);
        return customerService.getCustomerById(customerId);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @PostMapping
    public @NotNull ResponseEntity createCustomer(@RequestBody @NotNull Customer customer) {
        log.debug("Create customer - Controller: {}", customer.getCustomerName());
        Customer savedCustomer = customerService.saveNewCustomer(customer);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.LOCATION, "/api/v1/customer/" + savedCustomer.getId());
        return new ResponseEntity(headers, HttpStatus.CREATED);

    }

}
