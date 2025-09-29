package dev.dead.spring6restmvc.controllers;

import dev.dead.spring6restmvc.models.BeerDTO;
import dev.dead.spring6restmvc.repositories.BeerRepository;
import dev.dead.spring6restmvc.services.BeerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BeerControllerIT {
    @Autowired
    BeerController beerController;
    @Autowired
    BeerRepository beerRepository;

    @Test
    void getBeers() {
        assertNotNull(beerController);
        assertNotNull(beerRepository);
        List<BeerDTO> dtos = beerController.getBeers();
        assertEquals(3, dtos.size());
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
}