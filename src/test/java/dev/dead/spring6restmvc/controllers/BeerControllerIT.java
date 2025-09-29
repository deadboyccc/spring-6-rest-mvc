package dev.dead.spring6restmvc.controllers;

import dev.dead.spring6restmvc.entities.Beer;
import dev.dead.spring6restmvc.models.BeerDTO;
import dev.dead.spring6restmvc.repositories.BeerRepository;
import dev.dead.spring6restmvc.services.BeerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BeerControllerIT {
    @Autowired
    BeerController beerController;

    @Autowired
    BeerRepository beerRepository;

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