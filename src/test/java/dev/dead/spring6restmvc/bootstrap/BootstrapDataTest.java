package dev.dead.spring6restmvc.bootstrap;

import dev.dead.spring6restmvc.repositories.BeerRepository;
import dev.dead.spring6restmvc.repositories.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BootstrapDataTest {

    @Autowired
    BeerRepository beerRepository;
    @Autowired
    CustomerRepository customerRepository;
    BootstrapData bootstrapData;


    @BeforeEach
    void setUp() {
        bootstrapData = new BootstrapData(beerRepository, customerRepository);

    }

    @Test
    void run() throws Exception {
        bootstrapData.run(null);
        assertEquals(3, beerRepository.count());
        assertEquals(3, customerRepository.count());
    }
}