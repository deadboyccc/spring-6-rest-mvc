package dev.dead.spring6restmvc.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.dead.spring6restmvc.models.Customer;
import dev.dead.spring6restmvc.services.CustomerService;
import dev.dead.spring6restmvc.services.CustomerServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    private final String basePath = "/api/v1/customer";
    @MockitoBean
    CustomerService customerService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    CustomerServiceImpl customerServiceImpl;

    @BeforeEach
    void setUp() {
        customerServiceImpl = new CustomerServiceImpl();
    }

    @AfterEach
    void tearDown() {
        customerServiceImpl = null;
    }

    @Test
    void createNewCustomer() throws Exception {
        Customer customerPostDto = Customer.builder()
                .customerName("TEST")
                .build();
        Customer returnedCustomerEntity = Customer.builder()
                .id(UUID.randomUUID())
                .customerName(customerPostDto.getCustomerName())
                .build();
        given(customerService.saveNewCustomer(any(Customer.class)))
                .willReturn(returnedCustomerEntity);
        mockMvc.perform(post(basePath)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerPostDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(header().string("Location",
                        basePath
                                + "/"
                                + (returnedCustomerEntity.getId().toString())));
    }

    @Test
    void getCustomers() throws Exception {
        given(customerService.getCustomers()).willReturn(customerServiceImpl.getCustomers());
        mockMvc.perform(get(basePath).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(customerServiceImpl.getCustomers().size())));
    }

    @Test
    void getCustomer() throws Exception {
        Customer customer = customerServiceImpl.getCustomers().get(0);
        given(customerService.getCustomerById(customer.getId())).willReturn(customer);
        mockMvc.perform(get(basePath + "/{customerId}", customer.getId()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(customer.getId().toString()))
                .andExpect(jsonPath("$.customerName").value(customer.getCustomerName()));
    }
}