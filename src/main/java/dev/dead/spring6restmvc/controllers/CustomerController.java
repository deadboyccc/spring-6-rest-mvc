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

    @PatchMapping("{customerId}")
    public ResponseEntity<Customer> patchCustomer(@PathVariable("customerId") UUID customerId, @RequestBody Customer customer) {
        log.debug("Patching customer with id - Controller {}", customerId);
        customerService.patchCustomer(customerId, customer);
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("{customerId}")
    public ResponseEntity deleteCustomer(@PathVariable("customerId") UUID customerId) {
        log.debug("Delete customer by id - Controller {}", customerId);
        customerService.deleteCustomerById(customerId);
        return ResponseEntity.noContent().build();
    }

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
