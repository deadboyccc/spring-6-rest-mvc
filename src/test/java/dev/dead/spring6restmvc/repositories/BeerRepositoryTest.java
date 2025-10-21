package dev.dead.spring6restmvc.repositories;

import dev.dead.spring6restmvc.bootstrap.BootstrapDataTest;
import dev.dead.spring6restmvc.entities.Beer;
import dev.dead.spring6restmvc.models.BeerStyle;
import dev.dead.spring6restmvc.services.BeerCsvServiceImpl;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({BootstrapDataTest.class, BeerCsvServiceImpl.class})
class BeerRepositoryTest {
    @Autowired
    BeerRepository beerRepository;

    @Test
    void testGetBeersQueryByBeerName() {
        var beers = beerRepository.findAllByBeerNameIsLikeIgnoreCase("%IPA%");
        assertNotNull(beers);
        assertEquals(336, beers.size());

    }

    @Rollback
    @Transactional
    @Test
    void testSaveBeerNameTooLongEntityValidation() {


        assertThrows(ConstraintViolationException.class, () -> {

            Beer savedBeer = beerRepository.saveAndFlush(
                    Beer.builder()
                            .beerName(UUID.randomUUID()
                                    .toString()
                                    .repeat(20))
                            .beerStyle(BeerStyle.LAGER)
                            .upc(UUID.randomUUID()
                                    .toString())
                            .price(new BigDecimal(12))
                            .quantityOnHand(12)
                            .build()
            );
        });
    }

    @Rollback
    @Transactional
    @Test
    void testSaveBeer() {
        Beer savedBeer = beerRepository.saveAndFlush(
                Beer.builder()
                        .beerName("Beer Name")
                        .beerStyle(BeerStyle.LAGER)
                        .upc(UUID.randomUUID()
                                .toString())
                        .price(new BigDecimal(12))
                        .quantityOnHand(12)
                        .build()
        );
        assertNotNull(savedBeer);
        assertNotNull(savedBeer.getId());
    }

}