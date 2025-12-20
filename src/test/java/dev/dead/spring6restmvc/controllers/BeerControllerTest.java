package dev.dead.spring6restmvc.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.dead.spring6restmvc.config.SpringSecConfig;
import dev.dead.spring6restmvc.entities.Beer;
import dev.dead.spring6restmvc.models.BeerDTO;
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
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@Slf4j
@WebMvcTest(BeerController.class)
@Import(SpringSecConfig.class)
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
    ArgumentCaptor<BeerDTO> beerArgumentCaptor;
    public static RequestPostProcessor postProcessor = httpBasic("user", "password");

    @BeforeEach
    void setUp() {
        beerServiceImpl = new BeerServiceImpl();
    }

    @AfterEach
    void tearDown() {
        beerServiceImpl = null;
    }

    @Test
    void testUpdateBeerNegativePrice() throws Exception {
        Beer beer = returnBeerEntity();
        BeerDTO beerDTO = beerServiceImpl.getBeers(null, null, false, 1, 25)
                .getContent()
                .get(0);
        beerDTO.setPrice(new BigDecimal("-10.99"));
        given(beerService.updateBeer(any(UUID.class), any(BeerDTO.class)))
                .willReturn(Optional.of(beerDTO));
        mockMvc.perform(put(BeerController.BEER_ID_URL, beer.getId())
                        .with(postProcessor)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beerDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.length()", is(1)));
    }

    @Test
    void testUpdateBeerBlankBeerNameValidation() throws Exception {
        Beer beer = returnBeerEntity();
        BeerDTO beerDTO = beerServiceImpl.getBeers(null, null, false, 1, 25)
                .getContent()
                .get(0);
        beerDTO.setBeerName("      ");
        given(beerService.updateBeer(any(UUID.class), any(BeerDTO.class)))
                .willReturn(Optional.of(beerDTO));
        mockMvc.perform(put(BeerController.BEER_ID_URL, beer.getId())
                        .with(postProcessor)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beerDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.length()", is(1)));
    }

    @Test
    void testCreateBeerNegativePrice() throws Exception {
        BeerDTO beerDTO = beerServiceImpl.getBeers(null, null, false, 1, 25)
                .getContent()
                .get(0);
        beerDTO.setPrice(new BigDecimal("-10.99"));
        given(beerService.saveNewBeer(any(BeerDTO.class)))
                .willReturn(beerServiceImpl.getBeers(null, null, false, 1, 25)
                        .getContent()
                        .get(0));
        mockMvc.perform(post(BeerController.BEER_BASE_URL)
                        .with(postProcessor)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beerDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }

    @Test
    void testCreateBeerBlankBeerNameValidation() throws Exception {
        BeerDTO beerDTO = beerServiceImpl.getBeers(null, null, false, 1, 25)
                .getContent()
                .get(0);
        beerDTO.setBeerName("      ");
        given(beerService.saveNewBeer(any(BeerDTO.class)))
                .willReturn(beerServiceImpl.getBeers(null, null, false, 1, 25)
                        .getContent()
                        .get(0));
        mockMvc.perform(post(BeerController.BEER_BASE_URL)
                        .with(postProcessor)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beerDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }

    @Test
    void testCreateBeerNotNullBeerNameValidation() throws Exception {

        BeerDTO beerDTO = BeerDTO.builder()
                .build();

        given(beerService.saveNewBeer(any(BeerDTO.class)))
                .willReturn(beerServiceImpl.getBeers(null, null, false, 1, 25)
                        .getContent()
                        .get(0));
        MvcResult mvcResult = mockMvc.perform(post(BeerController.BEER_BASE_URL)
                        .with(postProcessor)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beerDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                //NotBlank - NotNull
                .andExpect(jsonPath("$.length()", is(6)))
                .andReturn();

        log.debug("mvcResult: {}", mvcResult.getResponse()
                .getContentAsString());
    }

    @Test
    void testPatchBeerById() throws Exception {

        // Given
        assertNotNull(beerServiceImpl);
        UUID beerId = beerServiceImpl.getBeers(null, null, false, 1, 25)
                .getContent()
                .get(0)
                .getId();

        BeerDTO beerDTO = BeerDTO.builder()
                .beerName("NEW BEER TEST")
                .build();

        given(beerService.patchBeerById(any(UUID.class), any(BeerDTO.class))).willReturn(Optional.of(beerDTO));
        // When
        mockMvc.perform(patch(BeerController.BEER_ID_URL, beerId)
                        .with(postProcessor)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beerDTO)))
                .andExpect(status().isNoContent());

        // Then
        verify(beerService).patchBeerById(uuidArgumentCaptor.capture(), beerArgumentCaptor.capture());
        assertEquals(beerDTO.getBeerName(), beerArgumentCaptor.getValue()
                .getBeerName());
        assertEquals(beerId, uuidArgumentCaptor.getValue());
    }

    @Test
    void testUpdateBeerById() throws Exception {
        // Given
        assertNotNull(beerServiceImpl);
        UUID beerId = beerServiceImpl.getBeers(null, null, false, 1, 25)
                .getContent()
                .get(0)
                .getId();
        BeerDTO beerDTO = BeerDTO.builder()
                .beerName("NEW BEER TEST")
                .beerStyle(BeerStyle.PALE_ALE)
                .upc("123456")
                .price(new BigDecimal("10.99"))
                .quantityOnHand(24)
                .build();

        // When
        given(beerService.updateBeer(any(UUID.class), any(BeerDTO.class)))
                .willReturn(Optional.of(beerDTO));

        // Then
        mockMvc.perform(put(BeerController.BEER_ID_URL, beerId)
                        .with(postProcessor)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beerDTO)))
                .andExpect(status().isNoContent())
                .andExpect(header().exists("Location"))
                .andExpect(header().string("Location", BeerController.BEER_BASE_URL + "/" + beerId.toString()));
        verify(beerService).updateBeer(any(UUID.class), any(BeerDTO.class));
    }

    @Test
    void testDeleteBeerById() throws Exception {
        //when 
        assertNotNull(beerServiceImpl);
        UUID beerId = beerServiceImpl.getBeers(null, null, false, 1, 25)
                .getContent()
                .get(0)
                .getId();

        given(beerService.deleteBeerById(any(UUID.class))).willReturn(true);
        //then
        mockMvc.perform(delete(BeerController.BEER_ID_URL, beerId)
                        .with(postProcessor)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        verify(beerService).deleteBeerById(uuidArgumentCaptor.capture());
        assertEquals(beerId.toString(), uuidArgumentCaptor.getValue()
                .toString());


    }

    @Test
    void testCreateNewBeer() throws Exception {
        // mimicking dto
        BeerDTO beerDTO = BeerDTO.builder()
                .beerName("NEW BEER TEST")
                .beerStyle(BeerStyle.PALE_ALE)
                .upc("123456")
                .price(new BigDecimal("10.99"))
                .quantityOnHand(24)
                .build();

        BeerDTO savedBeerDTO = BeerDTO.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName(beerDTO.getBeerName())
                .beerStyle(beerDTO.getBeerStyle())
                .upc(beerDTO.getUpc())
                .price(beerDTO.getPrice())
                .quantityOnHand(beerDTO.getQuantityOnHand())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        given(beerService.saveNewBeer(any(BeerDTO.class)))
                .willReturn(savedBeerDTO);

        mockMvc.perform(post(BeerController.BEER_BASE_URL)
                        .with(postProcessor)
                        .content(objectMapper.writeValueAsString(beerDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(header().string("Location", "/api/v1/beer/" + savedBeerDTO.getId()
                        .toString()));
    }

    @Test
    void testJacksonConfig() throws JsonProcessingException {
        assertNotNull(beerServiceImpl);
        BeerDTO beerDTO = beerServiceImpl.getBeers(null, null, false, 1, 25)
                .getContent()
                .get(0);
        String jsonString = objectMapper.writeValueAsString(beerDTO);
        assertEquals(beerDTO, objectMapper.readValue(jsonString, BeerDTO.class));
    }

    @Test
    void getBeerByIdNotFound() throws Exception {
        given(beerService.getBeerById(any(UUID.class))).willReturn(Optional.empty());
        mockMvc.perform(get(BeerController.BEER_ID_URL, UUID.randomUUID())
                        .with(postProcessor))
                .andExpect(status().isNotFound());
    }

    @Test
    void getBeerByIdTest() throws Exception {
        assertNotNull(beerServiceImpl);
        BeerDTO beerDTO = beerServiceImpl.getBeers(null, null, false, 1, 25)
                .getContent()
                .get(0);
        given(beerService.getBeerById(beerDTO.getId())).willReturn(Optional.of(beerDTO));


        mockMvc.perform(get(BeerController.BEER_ID_URL, beerDTO.getId())
                        .with(postProcessor)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(beerDTO.getId()
                        .toString()))
                .andExpect(jsonPath("$.beerName").value(beerDTO.getBeerName()))
                .andExpect(jsonPath("$.beerStyle").value(beerDTO.getBeerStyle()
                        .toString()));
    }

    @Test
    void getBeersTest() throws Exception {
        assertNotNull(beerServiceImpl);
        given(beerService.getBeers(any(), any(), any(), any(), any())).willReturn(beerServiceImpl.getBeers(null, null, false, 1, 25));
        postProcessor = SecurityMockMvcRequestPostProcessors.httpBasic("user", "password");
        mockMvc.perform(get(BeerController.BEER_BASE_URL).accept(MediaType.APPLICATION_JSON)
                        .with(
                                postProcessor
                        ))

                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content.length()", is(beerServiceImpl.getBeers(null, null, false, 1, 25)
                        .getContent()
                        .size())));


    }

    Beer returnBeerEntity() {
        return Beer.builder()
                .id(UUID.randomUUID())
                .beerName("New Beer")
                .beerStyle(BeerStyle.LAGER)
                .price(new BigDecimal(12))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .version(0)
                .quantityOnHand(12)
                .build();

    }

//
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

