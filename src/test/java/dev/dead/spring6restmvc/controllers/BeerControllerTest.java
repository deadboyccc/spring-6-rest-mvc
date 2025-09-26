package dev.dead.spring6restmvc.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.dead.spring6restmvc.models.Beer;
import dev.dead.spring6restmvc.models.BeerStyle;
import dev.dead.spring6restmvc.services.BeerService;
import dev.dead.spring6restmvc.services.BeerServiceImpl;
import lombok.extern.slf4j.Slf4j;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@Slf4j
@WebMvcTest(BeerController.class)
class BeerControllerTest {

    @Autowired
    MockMvc mockMvc;
    @MockitoBean
    BeerService beerService;
    @Nullable BeerServiceImpl beerServiceImpl;
    @Autowired
    ObjectMapper objectMapper;
    @Captor
    ArgumentCaptor<UUID> uuidArgumentCaptor;
    @Captor
    ArgumentCaptor<Beer> beerArgumentCaptor;

    @BeforeEach
    void setUp() {
        beerServiceImpl = new BeerServiceImpl();
    }

    @AfterEach
    void tearDown() {
        beerServiceImpl = null;
    }


    @Test
    void testPatchBeerById() throws Exception {

        // Given
        assertNotNull(beerServiceImpl);
        UUID beerId = beerServiceImpl.getBeers()
                .get(0)
                .getId();

        Beer beer = Beer.builder()
                .beerName("NEW BEER TEST")
                .build();

        // When
        mockMvc.perform(patch(BeerController.BEER_ID_URL, beerId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beer)))
                .andExpect(status().isNoContent());

        // Then
        verify(beerService).patchBeerById(uuidArgumentCaptor.capture(), beerArgumentCaptor.capture());
        assertEquals(beer.getBeerName(), beerArgumentCaptor.getValue()
                .getBeerName());
        assertEquals(beerId, uuidArgumentCaptor.getValue());
    }

    @Test
    void testUpdateBeerById() throws Exception {
        // Given
        assertNotNull(beerServiceImpl);
        UUID beerId = beerServiceImpl.getBeers()
                .get(0)
                .getId();
        Beer beer = Beer.builder()
                .beerName("NEW BEER TEST")
                .beerStyle(BeerStyle.PALE_ALE)
                .upc("123456")
                .price(new BigDecimal("10.99"))
                .quantityOnHand(24)
                .build();

        // When
        given(beerService.updateBeer(any(UUID.class), any(Beer.class)))
                .willReturn(beer);

        // Then
        mockMvc.perform(put(BeerController.BEER_ID_URL, beerId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beer)))
                .andExpect(status().isNoContent())
                .andExpect(header().exists("Location"))
                .andExpect(header().string("Location", BeerController.BEER_BASE_URL + "/" + beerId.toString()));
        verify(beerService).updateBeer(any(UUID.class), any(Beer.class));
    }

    @Test
    void testDeleteBeerById() throws Exception {
        //when 
        assertNotNull(beerServiceImpl);
        UUID beerId = beerServiceImpl.getBeers()
                .get(0)
                .getId();


        //then
        mockMvc.perform(delete(BeerController.BEER_ID_URL, beerId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        verify(beerService).deleteBeerById(uuidArgumentCaptor.capture());
        assertEquals(beerId.toString(), uuidArgumentCaptor.getValue()
                .toString());


    }

    @Test
    void testCreateNewBeer() throws Exception {
        // mimicking dto
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

        given(beerService.saveNewBeer(any(Beer.class)))
                .willReturn(savedBeer);

        mockMvc.perform(post(BeerController.BEER_BASE_URL)
                        .content(objectMapper.writeValueAsString(beer))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(header().string("Location", "/api/v1/beer/" + savedBeer.getId()
                        .toString()));
    }

    void testJacksonConfig() throws JsonProcessingException {
        assertNotNull(beerServiceImpl);
        Beer beer = beerServiceImpl.getBeers()
                .get(0);
        String jsonString = objectMapper.writeValueAsString(beer);
        assertEquals(beer, objectMapper.readValue(jsonString, Beer.class));
    }

    @Test
    void getBeerByIdTest() throws Exception {
        assertNotNull(beerServiceImpl);
        Beer beer = beerServiceImpl.getBeers()
                .get(0);
        given(beerService.getBeerById(beer.getId())).willReturn(beer);


        mockMvc.perform(get(BeerController.BEER_ID_URL, beer.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(beer.getId()
                        .toString()))
                .andExpect(jsonPath("$.beerName").value(beer.getBeerName()))
                .andExpect(jsonPath("$.beerStyle").value(beer.getBeerStyle()
                        .toString()));
    }

    @Test
    void getBeersTest() throws Exception {
        assertNotNull(beerServiceImpl);
        given(beerService.getBeers()).willReturn(beerServiceImpl.getBeers());
        mockMvc.perform(get(BeerController.BEER_BASE_URL).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(beerServiceImpl.getBeers()
                        .size())));


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