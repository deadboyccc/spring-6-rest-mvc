package dev.dead.spring6restmvc.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.dead.spring6restmvc.models.Customer;
import dev.dead.spring6restmvc.services.CustomerService;
import dev.dead.spring6restmvc.services.CustomerServiceImpl;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    private final String basePath = "/api/v1/customer";
    @MockitoBean
    CustomerService customerService;
    @Nullable CustomerServiceImpl customerServiceImpl;
    @Captor
    ArgumentCaptor<UUID> captor;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        customerServiceImpl = new CustomerServiceImpl();
    }

    @AfterEach
    void tearDown() {
        customerServiceImpl = null;
    }


    @Test
    void testDeleteCustomerById() throws Exception {
        //when
        UUID customerId = customerServiceImpl.getCustomers()
                .get(0)
                .getId();
        //then
        mockMvc.perform(delete(basePath + "/{customerId}", customerId.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent()
                );
        verify(customerService).deleteCustomerById(captor.capture());

        assertEquals(customerId.toString(), captor.getValue()
                .toString());
    }

    @Test
    void testUpdateCustomer() throws Exception {
        // when
        UUID customerId = customerServiceImpl.getCustomers()
                .get(0)
                .getId();
        Customer customer = customerServiceImpl.getCustomers()
                .get(0);
        // given
        given(customerService.updateCustomerById(any(UUID.class), any(Customer.class)))
                .willReturn(null);

        // then
        mockMvc.perform(put(basePath + "/" + customerId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isNoContent())
                .andExpect(header().exists("Location"))
                .andExpect(header().string("Location", basePath + "/" + customerId.toString()));
        verify(customerService).updateCustomerById(captor.capture(), any(Customer.class));

        assertEquals(customerId.toString(), captor.getValue()
                .toString());
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
                                + (returnedCustomerEntity.getId()
                                .toString())));
    }

    @Test
    void getCustomers() throws Exception {
        given(customerService.getCustomers()).willReturn(customerServiceImpl.getCustomers());
        mockMvc.perform(get(basePath).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(customerServiceImpl.getCustomers()
                        .size())));
    }

    @Test
    void getCustomer() throws Exception {
        Customer customer = customerServiceImpl.getCustomers()
                .get(0);
        given(customerService.getCustomerById(customer.getId())).willReturn(customer);
        mockMvc.perform(get(basePath + "/{customerId}", customer.getId()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(customer.getId()
                        .toString()))
                .andExpect(jsonPath("$.customerName").value(customer.getCustomerName()));
    }
}