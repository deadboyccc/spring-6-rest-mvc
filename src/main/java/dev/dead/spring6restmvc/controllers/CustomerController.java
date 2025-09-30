package dev.dead.spring6restmvc.controllers;

import dev.dead.spring6restmvc.models.CustomerDTO;
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
    public @NotNull ResponseEntity<CustomerDTO> patchCustomer(@PathVariable("customerId") UUID customerId,
                                                              @RequestBody CustomerDTO customerDTO) {
        log.debug("Patching customer with id - Controller {}", customerId);
        customerService.patchCustomer(customerId, customerDTO);
        return ResponseEntity.noContent()
                .build();
    }

    @DeleteMapping(CUSTOMER_ID_URL)
    public @NotNull ResponseEntity deleteCustomer(@PathVariable("customerId") UUID customerId) {
        log.debug("Delete customer by id - Controller {}", customerId);
        if (customerService.deleteCustomerById(customerId)) {

            return ResponseEntity.noContent()
                    .build();
        }
        throw new NotFoundException();
    }

    @PutMapping(CUSTOMER_ID_URL)
    public @NotNull ResponseEntity<CustomerDTO> updateBeer(@PathVariable("customerId") @NotNull UUID customerId,
                                                           @RequestBody CustomerDTO customerDTO) {
        customerService.updateCustomerById(customerId, customerDTO);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add
                ("Location", "/api/v1/customer/" + customerId);
        return ResponseEntity
                .noContent()
                .headers(responseHeaders)
                .build();
    }

    @GetMapping(CUSTOMER_BASE_URL)
    public List<CustomerDTO> getCustomers() {
        log.debug("Get customers - Controller");
        return customerService.getCustomers();
    }

    @GetMapping(CUSTOMER_ID_URL)
    public CustomerDTO getCustomerById(@PathVariable("customerId") UUID customerId) {
        log.debug("Get customer by id - Controller: {}", customerId);
        return customerService.getCustomerById(customerId)
                .orElseThrow(NotFoundException::new);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @PostMapping(CUSTOMER_BASE_URL)
    public @NotNull ResponseEntity createCustomer(@RequestBody @NotNull CustomerDTO customerDTO) {
        log.debug("Create customer - Controller: {}", customerDTO.getCustomerName());
        CustomerDTO savedCustomerDTO = customerService.saveNewCustomer(customerDTO);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.LOCATION, "/api/v1/customer/" + savedCustomerDTO.getId());
        return new ResponseEntity(headers, HttpStatus.CREATED);

    }

}
