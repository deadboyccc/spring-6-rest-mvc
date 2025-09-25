package dev.dead.spring6restmvc.controllers;

import dev.dead.spring6restmvc.services.BeerService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Slf4j
@WebMvcTest(BeerController.class)
class BeerControllerTest {

    final String basePath = "/api/v1/beer";
    @Autowired
    MockMvc mockMvc;
    @MockitoBean
    BeerService beerService;

    @Test
    void getBeerByIdTest() throws Exception {
        final UUID beerId = UUID.randomUUID();

        mockMvc.perform(get(basePath + "/{beerId}", beerId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
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