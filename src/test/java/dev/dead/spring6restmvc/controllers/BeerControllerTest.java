package dev.dead.spring6restmvc.controllers;

import dev.dead.spring6restmvc.models.Beer;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
@SpringBootTest
class BeerControllerTest {
    @Autowired
     BeerController controller;




    @Test
    void getBeerById() {
        UUID id = UUID.randomUUID();
        Beer beer = controller.getBeerById(id);
        log.debug("get Beer By Id in Test. id: {}", id);
        log.debug("get Beer By Id in Test. beer: {}", beer);
        assertNotNull(beer);
    }
}