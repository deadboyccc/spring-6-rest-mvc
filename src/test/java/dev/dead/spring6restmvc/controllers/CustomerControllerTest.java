package dev.dead.spring6restmvc.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.dead.spring6restmvc.config.SpringSecConfig;
import dev.dead.spring6restmvc.models.CustomerDTO;
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
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(CustomerController.class)
@Import(SpringSecConfig.class)
class CustomerControllerTest {


    @MockitoBean
    CustomerService customerService;
    @Nullable CustomerServiceImpl customerServiceImpl;
    @Captor
    ArgumentCaptor<UUID> uuidArgumentCaptor;
    @Captor
    ArgumentCaptor<CustomerDTO> customerArgumentCaptor;
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
    void testPatchCustomerById() throws Exception {
        // when
        assertNotNull(customerServiceImpl);
        UUID customerId = customerServiceImpl.getCustomers()
                .get(0)
                .getId();
        assertNotNull(customerId);
        // -- customer dto --
        CustomerDTO customerDTO = CustomerDTO.builder()
                .customerName("NEW TEST NAME")
                .build();

        given(customerService.patchCustomer(any(UUID.class), any(CustomerDTO.class))).willReturn(Optional.of(customerDTO));
        // then
        mockMvc.perform(patch(CustomerController.CUSTOMER_ID_URL, customerId)
                        .with(BeerControllerTest.postProcessor)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerDTO)
                        ))
                .andExpect(status().isNoContent());
        verify(customerService, times(1)).patchCustomer(uuidArgumentCaptor.capture(), customerArgumentCaptor.capture());
        assertEquals(uuidArgumentCaptor.getValue(), customerId);
        assertEquals(customerArgumentCaptor.getValue()
                .getCustomerName(), customerDTO.getCustomerName());

    }

    @Test
    void testDeleteCustomerById() throws Exception {
        //when
        assertNotNull(customerServiceImpl);
        UUID customerId = customerServiceImpl.getCustomers()
                .get(0)
                .getId();
        //then
        given(customerService.deleteCustomerById(any(UUID.class))).willReturn(true);
        mockMvc.perform(delete(CustomerController.CUSTOMER_ID_URL, customerId)
                        .with(BeerControllerTest.postProcessor)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent()
                );
        verify(customerService).deleteCustomerById(uuidArgumentCaptor.capture());

        assertEquals(customerId.toString(), uuidArgumentCaptor.getValue()
                .toString());
    }

    @Test
    void testUpdateCustomer() throws Exception {
        // given
        assertNotNull(customerServiceImpl);
        UUID customerId = customerServiceImpl.getCustomers()
                .get(0)
                .getId();
        CustomerDTO customerDTO = CustomerDTO.builder()
                .customerName("NEW TEST NAME")
                .build();
        given(customerService.updateCustomerById(any(UUID.class), any(CustomerDTO.class)))
                .willReturn(Optional.of(customerDTO));

        // when + then
        mockMvc.perform(put(CustomerController.CUSTOMER_ID_URL, customerId)
                        .with(BeerControllerTest.postProcessor)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerDTO)))
                .andExpect(status().isNoContent())
                .andExpect(header().exists("Location"))
                .andExpect(header().string("Location", CustomerController.CUSTOMER_BASE_URL + "/" + customerId.toString()));

        // verify
        verify(customerService, times(1)).updateCustomerById(uuidArgumentCaptor.capture(), any(CustomerDTO.class));
        assertEquals(customerId.toString(), uuidArgumentCaptor.getValue()
                .toString());
    }

    @Test
    void createNewCustomer() throws Exception {
        CustomerDTO customerDTOPostDto = CustomerDTO.builder()
                .customerName("TEST")
                .build();
        CustomerDTO returnedCustomerDTOEntity = CustomerDTO.builder()
                .id(UUID.randomUUID())
                .customerName(customerDTOPostDto.getCustomerName())
                .build();
        given(customerService.saveNewCustomer(any(CustomerDTO.class)))
                .willReturn(returnedCustomerDTOEntity);
        mockMvc.perform(post(CustomerController.CUSTOMER_BASE_URL)
                        .with(BeerControllerTest.postProcessor)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerDTOPostDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(header().string("Location",
                        CustomerController.CUSTOMER_BASE_URL
                                + "/"
                                + (returnedCustomerDTOEntity.getId()
                                .toString())));
    }

    @Test
    void getCustomers() throws Exception {
        assertNotNull(customerServiceImpl);
        given(customerService.getCustomers()).willReturn(customerServiceImpl.getCustomers());
        mockMvc.perform(get(CustomerController.CUSTOMER_BASE_URL).accept(MediaType.APPLICATION_JSON)
                        .with(BeerControllerTest.postProcessor))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(customerServiceImpl.getCustomers()
                        .size())));
    }

    @Test
    void getCustomerByIdNotFound() throws Exception {
        given(customerService.getCustomerById(any(UUID.class))).willReturn(Optional.empty());
        mockMvc.perform(get(CustomerController.CUSTOMER_ID_URL,
                        UUID.randomUUID()).
                        with(BeerControllerTest.postProcessor))
                .andExpect(status().isNotFound());
    }

    @Test
    void getCustomerById() throws Exception {
        assertNotNull(customerServiceImpl);
        CustomerDTO customerDTO = customerServiceImpl.getCustomers()
                .get(0);
        given(customerService.getCustomerById(customerDTO.getId())).willReturn(Optional.of(customerDTO));
        mockMvc.perform(get(CustomerController.CUSTOMER_ID_URL, customerDTO.getId())
                        .with(BeerControllerTest.postProcessor)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(customerDTO.getId()
                        .toString()))
                .andExpect(jsonPath("$.customerName").value(customerDTO.getCustomerName()));
    }
}