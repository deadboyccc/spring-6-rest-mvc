package dev.dead.spring6restmvc.controllers;

import dev.dead.spring6restmvc.models.CustomerDTO;
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
@RequiredArgsConstructor
@RestController
public class CustomerController {
    public static final String CUSTOMER_BASE_URL = "/api/v1/customer";
    public static final String CUSTOMER_ID_URL = CUSTOMER_BASE_URL + "/{customerId}";
    private final CustomerService customerService;

    @PatchMapping(CUSTOMER_ID_URL)
    public ResponseEntity<CustomerDTO> patchCustomer(
            @PathVariable("customerId") UUID customerId,
                                                              @RequestBody CustomerDTO customerDTO) {
        log.debug("Patching customer with id - Controller {}", customerId);
        if (!customerService.patchCustomer(customerId, customerDTO)
                .isEmpty()) {
            return ResponseEntity.noContent()
                    .build();
        }
        throw new NotFoundException();
    }

    @DeleteMapping(CUSTOMER_ID_URL)
    public ResponseEntity deleteCustomerById(
            @PathVariable("customerId") UUID customerId) {
        log.debug("Delete customer by id - Controller {}", customerId);
        if (customerService.deleteCustomerById(customerId)) {

            return ResponseEntity.noContent()
                    .build();
        }
        throw new NotFoundException();
    }

    @PutMapping(CUSTOMER_ID_URL)
    public ResponseEntity<CustomerDTO> updateCustomerById(
            @PathVariable("customerId") UUID customerId,
                                                                   @RequestBody CustomerDTO customerDTO) {
        if (!customerService.updateCustomerById(customerId, customerDTO)
                .isEmpty()) {
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.add
                    ("Location", "/api/v1/customer/" + customerId);
            return ResponseEntity
                    .noContent()
                    .headers(responseHeaders)
                    .build();
        }
        throw new NotFoundException();
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
    public ResponseEntity createCustomer(@RequestBody CustomerDTO customerDTO) {
        log.debug("Create customer - Controller: {}", customerDTO.getCustomerName());
        CustomerDTO savedCustomerDTO = customerService.saveNewCustomer(customerDTO);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.LOCATION, "/api/v1/customer/" + savedCustomerDTO.getId());
        return new ResponseEntity(headers, HttpStatus.CREATED);

    }

}
