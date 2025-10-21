package dev.dead.spring6restmvc.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.dead.spring6restmvc.entities.Beer;
import dev.dead.spring6restmvc.mappers.BeerMapper;
import dev.dead.spring6restmvc.models.BeerDTO;
import dev.dead.spring6restmvc.models.BeerStyle;
import dev.dead.spring6restmvc.repositories.BeerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class BeerControllerIT {
    @Autowired
    BeerController beerController;

    @Autowired
    BeerRepository beerRepository;

    @Autowired
    BeerMapper beerMapper;

    @Autowired
    WebApplicationContext wac;

    MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .build();
    }

    @Rollback
    @Transactional
    @Test
    void testPatchBeerInvalidName() throws Exception {
        Beer beer = beerRepository.findAll()
                .get(0);

        // Create BeerDTO with a name that exceeds max length (50 characters)
        BeerDTO beerDto = BeerDTO.builder()
                .beerName("x".repeat(51))  // Create string with 51 characters
                .build();

        mockMvc.perform(patch(BeerController.BEER_ID_URL, beer.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beerDto)))
                .andExpect(status().isBadRequest());
    }

    @Rollback
    @Transactional
    @Test
    void patchFoundBeerById() {
        Beer beer = beerRepository.findAll()
                .get(0);
        BeerDTO beerDTO = beerMapper.beerToBeerDTO(beer);
        beerDTO.setBeerName("New Beer Name");
        ResponseEntity responseEntity = beerController.updateBeerById(beer.getId(), beerDTO);
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        Beer updatedBeer = beerRepository.findById(beer.getId())
                .get();
        assertNotNull(updatedBeer);
        assertEquals(beerDTO.getBeerName(), updatedBeer.getBeerName());
    }

    @Test
    void patchNotFoundBeerById() {
        assertThrows(NotFoundException.class, () ->
        {
            beerController.updateBeerById(UUID.randomUUID(), returnBeerDto());
        });

    }

    @Test
    void deleteNotFoundBeerById() {
        assertThrows(NotFoundException.class,
                () -> beerController.deleteBeerById(UUID.randomUUID()));


    }

    @Rollback
    @Transactional
    @Test
    void deleteFoundBeerById() {
        Beer beer = beerRepository.findAll()
                .get(0);

        ResponseEntity responseEntity = beerController.deleteBeerById(beer.getId());


        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        assertTrue(beerRepository.findById(beer.getId())
                .isEmpty());
        assertThrows(NotFoundException.class, () -> beerController.getBeerById(beer.getId()));
    }

    @Test
    void updateNotFoundBeerById() {
        assertThrows(NotFoundException.class,
                () -> beerController.updateBeerById(UUID.randomUUID(), returnBeerDto()));
    }


    @Rollback
    @Transactional
    @Test
    void updateFoundBeerById() {
        Beer beer = beerRepository.findAll()
                .get(0);
        BeerDTO beerDTO = beerMapper.beerToBeerDTO(beer);
        beerDTO.setBeerName("New Beer Name");
        beerDTO.setVersion(null);
        beerDTO.setCreatedAt(null);
        beerDTO.setUpdatedAt(null);
        beerDTO.setId(null);

        ResponseEntity responseEntity = beerController.updateBeerById(beer.getId(), beerDTO);
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());

        Beer savedBeer = beerRepository.findById(beer.getId())
                .get();
        assertNotNull(savedBeer);
        assertEquals(beerDTO.getBeerName(), savedBeer.getBeerName());
        assertEquals(1, savedBeer.getVersion());


    }

    @Rollback
    @Transactional
    @Test
    void saveNewBeer() {
        BeerDTO beerDTO = returnBeerDto();
        ResponseEntity responseEntity = beerController.addBeer(beerDTO);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getHeaders()
                .getLocation());

        var locationPath = responseEntity.getHeaders()
                .getLocation()
                .getPath();

        UUID savedUUID = UUID.fromString(locationPath.split("/")[4]);
        Beer savedBeer = beerRepository.findById(savedUUID)
                .get();
        assertNotNull(savedBeer);


        assertEquals(beerDTO.getBeerName(), savedBeer.getBeerName());
        assertEquals(beerDTO.getBeerStyle(), savedBeer.getBeerStyle());
        assertEquals(beerDTO.getPrice(), savedBeer.getPrice());
        assertEquals(beerDTO.getQuantityOnHand(), savedBeer.getQuantityOnHand());
        assertEquals(beerDTO.getUpc(), savedBeer.getUpc());
        assertEquals(0, savedBeer.getVersion());
        // refactor
        assertNotNull(savedBeer.getCreatedAt());
        assertNotNull(savedBeer.getUpdatedAt());


    }

    @Test
    void getBeerByIdNotFound() {
        assertThrows(NotFoundException.class, () -> {
            beerController.getBeerById(UUID.randomUUID());
        });
    }

    @Test
    void getBeerByIdFound() {
        Beer beer = beerRepository.findAll()
                .get(0);
        BeerDTO beerDTO = beerController.getBeerById(beer.getId());

        assertNotNull(beerDTO);

        assertEquals(beer.getId(), beerDTO.getId());
        assertEquals(beer.getBeerName(), beerDTO.getBeerName());
        assertEquals(beer.getBeerStyle(), beerDTO.getBeerStyle());
        assertEquals(beer.getPrice(), beerDTO.getPrice());
        assertEquals(beer.getQuantityOnHand(), beerDTO.getQuantityOnHand());
        assertEquals(beer.getUpc(), beerDTO.getUpc());
    }

    @Test
    void getBeers() {
        List<BeerDTO> dtos = beerController.getBeers();
        assertTrue(dtos.size() > 1000);
    }

    @Rollback
    @Transactional
    @Test
    void getBeersEmptyList() {
        beerRepository.deleteAll();
        beerRepository.flush();

        assertNotNull(beerController);
        assertNotNull(beerRepository);
        List<BeerDTO> dtos = beerController.getBeers();
        assertEquals(0, dtos.size());
    }

    BeerDTO returnBeerDto() {

        // uuid+version not set | timestamp set (need change)
        return BeerDTO.builder()
                .beerName("New Beer")
                .beerStyle(BeerStyle.LAGER)
                .upc(UUID.randomUUID()
                        .toString())
                .price(new BigDecimal(12))
                .quantityOnHand(12)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

    }

    Beer returnBeerEntity() {


        // uuid+version not set | timestamp set (need change)
        return Beer.builder()
                .beerName("New Beer")
                .beerStyle(BeerStyle.LAGER)
                .upc(UUID.randomUUID()
                        .toString())
                .price(new BigDecimal(12))
                .quantityOnHand(12)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}