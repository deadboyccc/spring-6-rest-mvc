package dev.dead.spring6restmvc.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.dead.spring6restmvc.entities.Beer;
import dev.dead.spring6restmvc.mappers.BeerMapper;
import dev.dead.spring6restmvc.models.BeerDTO;
import dev.dead.spring6restmvc.models.BeerStyle;
import dev.dead.spring6restmvc.repositories.BeerRepository;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.core.IsNull;
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
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
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

    @Test
    void tesListBeersByStyleAndNameShowInventoryTruePage1() throws Exception {
        mockMvc.perform(get(BeerController.BEER_BASE_URL)
                        .queryParam("beerName", "IPA")
                        .queryParam("beerStyle", BeerStyle.IPA.name())
                        .queryParam("showInventory", "true")
                        .queryParam("pageNumber", "2")
                        .queryParam("pageSize", "50"))
                .andExpect(status().isOk())
                // page content array should have up to 50 items
                .andExpect(jsonPath("$.content.length()", is(50)))
                .andExpect(jsonPath("$.content[0].quantityOnHand").value(IsNull.notNullValue()));
    }
    @Test
    void tesListBeersByStyleAndNameShowInventoryTrue() throws Exception {
        long expectedCount = beerRepository
                .findAllByBeerNameIsLikeIgnoreCaseAndBeerStyle("%" + "IPA" + "%", BeerStyle.IPA, Pageable.unpaged())
                .getTotalElements();

        mockMvc.perform(get(BeerController.BEER_BASE_URL)
                        .queryParam("beerName", "IPA")
                        .queryParam("beerStyle", BeerStyle.IPA.name())
                        .queryParam("pageSize", "1000")
                        .queryParam("showInventory", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()", is((int) expectedCount)))
                .andExpect(jsonPath("$.content[0].quantityOnHand").value(IsNull.notNullValue()));
    }

    @Test
    void tesListBeersByStyleAndNameShowInventoryFalse() throws Exception {
        long expectedCount = beerRepository
                .findAllByBeerNameIsLikeIgnoreCaseAndBeerStyle("%" + "IPA" + "%", BeerStyle.IPA, Pageable.unpaged())
                .getTotalElements();

        mockMvc.perform(get(BeerController.BEER_BASE_URL)
                        .queryParam("beerName", "IPA")
                        .queryParam("beerStyle", BeerStyle.IPA.name())
                        .queryParam("pageSize", "1000")
                        .queryParam("showInventory", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()", is((int) expectedCount)))
                .andExpect(jsonPath("$.content[0].quantityOnHand").value(IsNull.nullValue()));
    }

    @Test
    void tesListBeersByStyleAndName() throws Exception {
        long expectedCount = beerRepository
                .findAllByBeerNameIsLikeIgnoreCaseAndBeerStyle("%" + "IPA" + "%", BeerStyle.IPA, Pageable.ofSize(1000))
                .getTotalElements();

        mockMvc.perform(get(BeerController.BEER_BASE_URL)
                        .queryParam("beerName", "IPA")
                        .queryParam("pageSize", "1000")
                        .queryParam("beerStyle", BeerStyle.IPA.name()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()", is((int) expectedCount)));
    }

@Test
void testListBeersQueryByBeerStyle() throws Exception {
    BeerStyle testStyle = BeerStyle.IPA;
    Page<Beer> beerPage = beerRepository.findAllByBeerStyle(testStyle, Pageable.unpaged());
    long expected = beerPage.getTotalElements();

    // Ensure we have test data
    assertTrue(expected > 0, "Test requires IPA beers in database");

    var mvcResult = mockMvc.perform(get(BeerController.BEER_BASE_URL)
                    .queryParam("beerStyle", testStyle.name())
                    .queryParam("pageNumber", "0")  // Explicitly set page number
                    .queryParam("pageSize", "1000"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.totalElements").value(expected))  // Verify total count
            .andReturn();

    String content = mvcResult.getResponse()
            .getContentAsString();
    var jsonNode = objectMapper.readTree(content);
    var list = jsonNode.get("content");

    // Verify the page content size matches what we got
    assertEquals(expected, list.size(),
            "Page content size should match expected or page size limit");

    // Verify all returned beers are actually IPAs
    for (int i = 0; i < list.size(); i++) {
        assertEquals(testStyle.name(), list.get(i)
                        .get("beerStyle")
                        .asText(),
                "All returned beers should be of style " + testStyle);
    }
}
    @Test
    void testListBeersQueryByBeerNameInvalidName() throws Exception {
        String uuid = UUID.randomUUID()
                .toString();
        String randomName = UUID.randomUUID()
                .toString();
        var request = mockMvc.perform(get(BeerController.BEER_BASE_URL)
                        .queryParam("beerName", randomName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()", is(0)))
                .andReturn()
                .getRequest();
        // get the param
        String beerName = request.getParameter("beerName");
        log.debug("beerName: {}", beerName);
        assertEquals(randomName, beerName);
    }

    @Test
    void testListBeersQueryByBeerName() throws Exception {
        String query = "IPA";
        long expected = beerRepository.findAllByBeerNameIsLikeIgnoreCase("%" + query + "%", Pageable.unpaged())
                .getTotalElements();

        var mvcResult = mockMvc.perform(get(BeerController.BEER_BASE_URL)
                        .queryParam("pageSize", "1000")
                        .queryParam("beerName", query))
                .andExpect(status().isOk())
                .andReturn();

        String content = mvcResult.getResponse()
                .getContentAsString();
        // map response to list and assert size
        var list = objectMapper.readTree(content).get("content");
        assertEquals(expected, list.size());
        // get the param
        String beerName = mvcResult.getRequest()
                .getParameter("beerName");
        log.debug("beerName: {}", beerName);
        log.debug("query: {}", query);
        log.debug("expected: {}", expected);
        assertEquals(query, beerName);
    }

    @Rollback
    @Transactional
    @Test
    void testPatchBeerInvalidName() throws Exception {
        Beer beer = beerRepository.findAll()
                .get(0);

        // Create BeerDTO with a name that exceeds max length (50 characters)
        BeerDTO beerDto = BeerDTO.builder()
                .beerName("x".repeat(51)) // Create string with 51 characters
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
        assertThrows(NotFoundException.class, () -> {
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
        var page = beerController.getBeers(null, null, false, 1, 25);
        assertTrue(page.getTotalElements() > 1000);
    }

    @Rollback
    @Transactional
    @Test
    void getBeersEmptyList() {
        beerRepository.deleteAll();
        beerRepository.flush();

        assertNotNull(beerController);
        assertNotNull(beerRepository);
        var page = beerController.getBeers(null, null, false, 1, 25);
        assertEquals(0, page.getTotalElements());
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
