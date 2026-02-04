package dev.dead.spring6restmvc.repositories;

import dev.dead.spring6restmvc.bootstrap.BootstrapData;
import dev.dead.spring6restmvc.bootstrap.BootstrapDataTest;
import dev.dead.spring6restmvc.entities.Beer;
import dev.dead.spring6restmvc.entities.BeerOrder;
import dev.dead.spring6restmvc.entities.BeerOrderShipment;
import dev.dead.spring6restmvc.entities.Customer;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class BeerOrderRepositoryTest {
    @Autowired
    BeerOrderRepository beerOrderRepository;
    @Autowired
    BeerRepository beerRepository;
    @Autowired
    CustomerRepository customerRepository;

    Customer testCustomer;
    Beer testBeer;


    @BeforeEach
    void setUp() throws Exception {
        testBeer = beerRepository.findAll()
                .get(0);
        testCustomer = customerRepository.findAll()
                .get(0);
    }

    @Test
    void testBeerOrders() {
        log.debug("beers counts: `{}`", beerRepository.count());
        log.debug("customers counts: `{}`", customerRepository.count());
        log.debug("beer orders counts: `{}`", beerOrderRepository.count());
        log.debug("test beer: `{}`", testBeer);
        log.debug("test customer: `{}`", testCustomer);
    }

    @Transactional
    @Test
    void testRelationshipPersistance() {
        BeerOrder beerOrder = BeerOrder.builder()
                .customerRef("Test customer reference")
                .customer(testCustomer)
                .beerOrderShipment(
                        BeerOrderShipment.builder()
                                .trackingNumber("234ii")
                                .build()
                )
                .build();
        BeerOrder savedBeerOrder = beerOrderRepository.save(beerOrder);
        log.debug("saved beer order: `{}`", savedBeerOrder);
    }
}