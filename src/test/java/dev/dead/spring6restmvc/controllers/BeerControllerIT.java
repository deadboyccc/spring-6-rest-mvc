package dev.dead.spring6restmvc.controllers;

import dev.dead.spring6restmvc.entities.Beer;
import dev.dead.spring6restmvc.models.BeerDTO;
import dev.dead.spring6restmvc.models.BeerStyle;
import dev.dead.spring6restmvc.repositories.BeerRepository;
import dev.dead.spring6restmvc.services.BeerService;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BeerControllerIT {
    @Autowired
    BeerController beerController;

    @Autowired
    BeerRepository beerRepository;

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