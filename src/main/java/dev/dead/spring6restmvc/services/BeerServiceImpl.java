package dev.dead.spring6restmvc.services;

import dev.dead.spring6restmvc.models.Beer;
import dev.dead.spring6restmvc.models.BeerStyle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
public class BeerServiceImpl implements BeerService {
    private Map<UUID, Beer> beers = new HashMap<>();

    public BeerServiceImpl() {
        Beer beer1 = Beer.builder()
                .id(UUID.randomUUID())
                .beerName("Galaxy Cat")
                .beerStyle(BeerStyle.PALE_ALE)
                .upc(UUID.randomUUID().toString())
                .quantityOnHand(3)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .price(new BigDecimal("11.25"))
                .version(1)
                .build();

        Beer beer2 = Beer.builder()
                .id(UUID.randomUUID())
                .beerName("Mango Bobs")
                .beerStyle(BeerStyle.IPA)
                .upc(UUID.randomUUID().toString())
                .quantityOnHand(5)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .price(new BigDecimal("12.99"))
                .version(1)
                .build();

        Beer beer3 = Beer.builder()
                .id(UUID.randomUUID())
                .beerName("Pinball Porter")
                .beerStyle(BeerStyle.PORTER)
                .upc(UUID.randomUUID().toString())
                .quantityOnHand(7)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .price(new BigDecimal("9.50"))
                .version(1)
                .build();

        beers.put(beer1.getId(), beer1);
        beers.put(beer2.getId(), beer2);
        beers.put(beer3.getId(), beer3);


    }

    @Override
    public List<Beer> getBeers() {
        return new ArrayList<>(beers.values());

    }

    @Override
    public Beer saveNewBeer(Beer beer) {
        log.debug("Save Beer - Service : {}", beer.getBeerName());
        Beer savedBeer = Beer.builder()
                .id(UUID.randomUUID())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .version(1)
                .price(beer.getPrice())
                .beerStyle(beer.getBeerStyle())
                .beerName(beer.getBeerName())
                .quantityOnHand(beer.getQuantityOnHand())
                .upc(beer.getUpc())
                .build();
        beers.put(savedBeer.getId(), savedBeer);
        return savedBeer;

    }

    @Override
    public Beer updateBeer(UUID beerId, Beer beer) {
        log.debug("Update Beer - Service id: {}", beer.getId());
        Beer existingBeer = beers.get(beerId);
        existingBeer.setBeerName(beer.getBeerName());
        existingBeer.setBeerStyle(beer.getBeerStyle());
        existingBeer.setQuantityOnHand(beer.getQuantityOnHand());
        existingBeer.setUpc(beer.getUpc());
        existingBeer.setPrice(beer.getPrice());
        existingBeer.setVersion(existingBeer.getVersion() + 1);
        return beers.replace(beerId, existingBeer);


    }


    @Override
    public Beer getBeerById(UUID id) {
        log.debug("Get Beer by Id - in service. Id: " + id.toString());
        return beers.get(id);
    }

}
