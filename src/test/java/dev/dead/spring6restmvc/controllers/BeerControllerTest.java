package dev.dead.spring6restmvc.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.dead.spring6restmvc.models.Beer;
import dev.dead.spring6restmvc.models.BeerStyle;
import dev.dead.spring6restmvc.services.BeerService;
import dev.dead.spring6restmvc.services.BeerServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@Slf4j
@WebMvcTest(BeerController.class)
class BeerControllerTest {

    @Autowired
    MockMvc mockMvc;
    @MockitoBean
    BeerService beerService;
    final String basePath = "/api/v1/beer";

    BeerServiceImpl beerServiceImpl = new BeerServiceImpl();
    @Autowired
    ObjectMapper objectMapper;

    @Test
    void testCreateNewBeer() throws Exception {
        Beer beer = Beer.builder()
                .beerName("NEW BEER TEST")
                .beerStyle(BeerStyle.PALE_ALE)
                .upc("123456")
                .price(new BigDecimal("10.99"))
                .quantityOnHand(24)
                .build();

        Beer savedBeer = Beer.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName(beer.getBeerName())
                .beerStyle(beer.getBeerStyle())
                .upc(beer.getUpc())
                .price(beer.getPrice())
                .quantityOnHand(beer.getQuantityOnHand())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        given(beerService.saveNewBeer(any(Beer.class))).willReturn(savedBeer);

        mockMvc.perform(post(basePath)
                        .content(objectMapper.writeValueAsString(beer))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(header().string("Location", "/api/v1/beer/" + savedBeer.getId().toString()));
    }

    void testJacksonConfig() throws JsonProcessingException {
        Beer beer = beerServiceImpl.getBeers().get(0);
        String jsonString = objectMapper.writeValueAsString(beer);
        assertEquals(beer, objectMapper.readValue(jsonString, Beer.class));
    }
    @Test
    void getBeerByIdTest() throws Exception {
        Beer beer = beerServiceImpl.getBeers().get(0);
        given(beerService.getBeerById(beer.getId())).willReturn(beer);


        mockMvc.perform(get(basePath + "/{beerId}", beer.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(beer.getId().toString()))
                .andExpect(jsonPath("$.beerName").value(beer.getBeerName()))
                .andExpect(jsonPath("$.beerStyle").value(beer.getBeerStyle().toString()))
                .andExpect(jsonPath("$.price").value(beer.getPrice()));
    }

    @Test
    void getBeersTest() throws Exception {
        given(beerService.getBeers()).willReturn(beerServiceImpl.getBeers());
        mockMvc.perform(get(basePath).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(beerServiceImpl.getBeers().size())));


    }


//@SpringBootTest
//    @Autowired
//     BeerController controller;
//    @Test
//    void OldgetBeerById() {
//        UUID id = UUID.randomUUID();
//        Beer beer = controller.getBeerById(id);
//        log.debug("get Beer By Id in Test. id: {}", id);
//        log.debug("get Beer By Id in Test. beer: {}", beer);
//        assertNotNull(beer);
//    }
}