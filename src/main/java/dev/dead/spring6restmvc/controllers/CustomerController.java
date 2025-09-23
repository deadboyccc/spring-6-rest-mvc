package dev.dead.spring6restmvc.controllers;

import dev.dead.spring6restmvc.models.Customer;
import dev.dead.spring6restmvc.services.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final CustomerService customerService;

    @PutMapping("{customerId}")
    public ResponseEntity<Customer> updateBeer(@PathVariable("customerId") UUID customerId, @RequestBody Customer customer) {
        customerService.updateCustomerById(customerId, customer);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add
                ("Location", "/api/v1/customer/" + customerId.toString());
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
    public ResponseEntity createCustomer(@RequestBody Customer customer) {
        log.debug("Create customer - Controller: {}", customer.getCustomerName());
        Customer savedCustomer = customerService.saveNewCustomer(customer);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.LOCATION, "/api/v1/customer/" + savedCustomer.getId());
        return new ResponseEntity(headers, HttpStatus.CREATED);

    }

}
