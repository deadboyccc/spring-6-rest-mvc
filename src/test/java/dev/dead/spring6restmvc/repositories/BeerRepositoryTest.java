package dev.dead.spring6restmvc.repositories;

import dev.dead.spring6restmvc.entities.Beer;
import dev.dead.spring6restmvc.models.BeerStyle;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BeerRepositoryTest {
    @Autowired
    BeerRepository beerRepository;

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