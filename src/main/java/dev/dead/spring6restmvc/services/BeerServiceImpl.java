package dev.dead.spring6restmvc.services;

import dev.dead.spring6restmvc.models.BeerDTO;
import dev.dead.spring6restmvc.models.BeerStyle;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
public class BeerServiceImpl implements BeerService {
    private final Map<UUID, BeerDTO> beers = new HashMap<>();

    public BeerServiceImpl() {
        BeerDTO beerDTO1 = BeerDTO.builder()
                .id(UUID.randomUUID())
                .beerName("Galaxy Cat")
                .beerStyle(BeerStyle.PALE_ALE)
                .upc(UUID.randomUUID()
                        .toString())
                .quantityOnHand(3)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .price(new BigDecimal("11.25"))
                .version(1)
                .build();

        BeerDTO beerDTO2 = BeerDTO.builder()
                .id(UUID.randomUUID())
                .beerName("Mango Bobs")
                .beerStyle(BeerStyle.IPA)
                .upc(UUID.randomUUID()
                        .toString())
                .quantityOnHand(5)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .price(new BigDecimal("12.99"))
                .version(1)
                .build();

        BeerDTO beerDTO3 = BeerDTO.builder()
                .id(UUID.randomUUID())
                .beerName("Pinball Porter")
                .beerStyle(BeerStyle.PORTER)
                .upc(UUID.randomUUID()
                        .toString())
                .quantityOnHand(7)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .price(new BigDecimal("9.50"))
                .version(1)
                .build();

        beers.put(beerDTO1.getId(), beerDTO1);
        beers.put(beerDTO2.getId(), beerDTO2);
        beers.put(beerDTO3.getId(), beerDTO3);


    }

    @Override
    public @NotNull List<BeerDTO> getBeers(String beerName) {
        log.debug("getBeers() - Service");
        return new ArrayList<>(beers.values());

    }

    @Override
    public @NotNull BeerDTO saveNewBeer(@NotNull BeerDTO beerDTO) {
        log.debug("Save Beer - Service : {}", beerDTO.getBeerName());
        BeerDTO savedBeerDTO = BeerDTO.builder()
                .id(UUID.randomUUID())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .version(1)
                .price(beerDTO.getPrice())
                .beerStyle(beerDTO.getBeerStyle())
                .beerName(beerDTO.getBeerName())
                .quantityOnHand(beerDTO.getQuantityOnHand())
                .upc(beerDTO.getUpc())
                .build();
        beers.put(savedBeerDTO.getId(), savedBeerDTO);
        return savedBeerDTO;

    }

    @Override
    public Optional<BeerDTO> updateBeer(UUID beerId, @NotNull BeerDTO beerDTO) {
        log.debug("Update Beer - Service id: {}", beerDTO.getId());
        BeerDTO existingBeerDTO = beers.get(beerId);
        existingBeerDTO.setBeerName(beerDTO.getBeerName());
        existingBeerDTO.setBeerStyle(beerDTO.getBeerStyle());
        existingBeerDTO.setQuantityOnHand(beerDTO.getQuantityOnHand());
        existingBeerDTO.setUpc(beerDTO.getUpc());
        existingBeerDTO.setPrice(beerDTO.getPrice());
        existingBeerDTO.setVersion(existingBeerDTO.getVersion() + 1);
        existingBeerDTO.setUpdatedAt(LocalDateTime.now());
        return Optional.of(existingBeerDTO);
        // redundant
//        return beers.replace(beerId, existingBeer);


    }

    @Override
    public Boolean deleteBeerById(UUID beerId) {
        log.debug("Delete Beer - Service id: {}", beerId);
        beers.remove(beerId);
        return true;

    }

    @Override
    public Optional<BeerDTO> patchBeerById(UUID beerId, @NotNull BeerDTO beerDTO) {
        log.debug("Patch Beer - Service id: {}", beerId);
        BeerDTO existingBeerDTO = beers.get(beerId);
        if (StringUtils.hasText(beerDTO.getBeerName())) {
            existingBeerDTO.setBeerName(beerDTO.getBeerName());

        }
        if (beerDTO.getBeerStyle() != null) {
            existingBeerDTO.setBeerStyle(beerDTO.getBeerStyle());
        }
        if (beerDTO.getQuantityOnHand() != null) {
            existingBeerDTO.setQuantityOnHand(beerDTO.getQuantityOnHand());
        }
        if (beerDTO.getUpc() != null) {
            existingBeerDTO.setUpc(beerDTO.getUpc());
        }
        if (beerDTO.getPrice() != null) {
            existingBeerDTO.setPrice(beerDTO.getPrice());

        }
        existingBeerDTO.setVersion(existingBeerDTO.getVersion() + 1);
        existingBeerDTO.setUpdatedAt(LocalDateTime.now());
        return Optional.of(existingBeerDTO);

    }


    @Override
    public Optional<BeerDTO> getBeerById(@NotNull UUID id) {
        log.debug("Get Beer by Id - in service. Id: {}", id);
        return Optional.of(beers.get(id));
    }

}
