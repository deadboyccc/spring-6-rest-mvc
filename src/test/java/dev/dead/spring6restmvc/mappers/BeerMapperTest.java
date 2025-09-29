package dev.dead.spring6restmvc.mappers;

import dev.dead.spring6restmvc.entities.Beer;
import dev.dead.spring6restmvc.models.BeerDTO;
import dev.dead.spring6restmvc.models.BeerStyle;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = BeerMapperImpl.class)
class BeerMapperTest {
    @Autowired
    BeerMapper beerMapper;

    @Test
    void fromBeerToBeerDto() {
        Beer beer = Beer.builder()
                .beerName("test")
                .quantityOnHand(12)
                .price(new BigDecimal(12))
                .upc(UUID.randomUUID()
                        .toString())
                .beerStyle(BeerStyle.LAGER)
                .build();

        BeerDTO beerDTO = beerMapper.beerToBeerDTO(beer);
        assertNotNull(beerDTO);
        assertEquals(beerDTO.getBeerName(), beer.getBeerName());
        assertEquals(beerDTO.getQuantityOnHand(), beer.getQuantityOnHand());
        assertEquals(beerDTO.getPrice(), beer.getPrice());
        assertEquals(beerDTO.getUpc(), beer.getUpc());
        assertEquals(beerDTO.getBeerStyle(), beer.getBeerStyle());
        assertInstanceOf(BeerDTO.class, beerDTO);
        assertInstanceOf(Beer.class, beer);

    }

    @Test
    void formBeerDTOtoBeer() {
        BeerDTO beerDTO = BeerDTO.builder()
                .beerName("name")
                .beerStyle(BeerStyle.LAGER)
                .price(new BigDecimal(12))
                .quantityOnHand(12)
                .upc(UUID.randomUUID()
                        .toString())
                .build();
        Beer beer = beerMapper.beerDTOToBeer(beerDTO);
        assertNotNull(beer);
        assertEquals(beerDTO.getBeerName(), beer.getBeerName());
        assertEquals(beerDTO.getQuantityOnHand(), beer.getQuantityOnHand());
        assertEquals(beerDTO.getPrice(), beer.getPrice());
        assertEquals(beerDTO.getUpc(), beer.getUpc());
        assertEquals(beerDTO.getBeerStyle(), beer.getBeerStyle());
        assertInstanceOf(BeerDTO.class, beerDTO);
        assertInstanceOf(Beer.class, beer);

    }

}