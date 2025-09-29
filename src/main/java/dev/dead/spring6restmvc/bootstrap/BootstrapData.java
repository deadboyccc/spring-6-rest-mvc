package dev.dead.spring6restmvc.bootstrap;

import dev.dead.spring6restmvc.entities.Beer;
import dev.dead.spring6restmvc.entities.Customer;
import dev.dead.spring6restmvc.models.BeerDTO;
import dev.dead.spring6restmvc.models.BeerStyle;
import dev.dead.spring6restmvc.models.CustomerDTO;
import dev.dead.spring6restmvc.repositories.BeerRepository;
import dev.dead.spring6restmvc.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class BootstrapData implements CommandLineRunner {
    private final BeerRepository beerRepository;
    private final CustomerRepository customerRepository;

    @Override
    public void run(String... args) throws Exception {

        loadBeerData();
        loadCustomerData();

    }

    private void loadBeerData() {
        if (beerRepository.count() > 0) {
            log.debug("Beer Data Already Loaded");
            return;
        }
        Beer beer1 = Beer.builder()
                .beerName("Galaxy Cat")
                .beerStyle(BeerStyle.PALE_ALE)
                .upc(UUID.randomUUID()
                        .toString())
                .quantityOnHand(3)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .price(new BigDecimal("11.25"))
                .build();

        Beer beer2 = Beer.builder()
                .beerName("Mango Bobs")
                .beerStyle(BeerStyle.IPA)
                .upc(UUID.randomUUID()
                        .toString())
                .quantityOnHand(5)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .price(new BigDecimal("12.99"))
                .build();

        Beer beer3 = Beer.builder()
                .beerName("Pinball Porter")
                .beerStyle(BeerStyle.PORTER)
                .upc(UUID.randomUUID()
                        .toString())
                .quantityOnHand(7)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .price(new BigDecimal("9.50"))
                .build();

        beerRepository.saveAllAndFlush(List.of(beer1, beer2, beer3));
        log.debug("Beer Data Loaded");
    }

    private void loadCustomerData() {
        if (customerRepository.count() > 0) {
            log.debug("Customer Data Already Loaded");
            return;
        }
        Customer customer1 = Customer.builder()
                .customerName("John")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        Customer customer2 = Customer.builder()
                .customerName("Joe")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Customer customer3 = Customer.builder()
                .customerName("Biden")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        customerRepository.saveAllAndFlush(List.of(customer1, customer2, customer3));
        log.debug("Customer Data Loaded");
    }

}
