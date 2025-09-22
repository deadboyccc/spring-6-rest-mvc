package dev.dead.spring6restmvc.services;

import dev.dead.spring6restmvc.models.Beer;
import dev.dead.spring6restmvc.models.BeerStyle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
public class BeerServiceImpl implements BeerService {
    @Override
    public Beer getBeerById(UUID id) {
        log.info("Get Beer by Id - in service. Id: " + id.toString());
        return Beer.builder()
                .id(id)
                .beerName("Galaxy Cat")
                .beerStyle(BeerStyle.PALE_ALE)
                .upc(UUID.randomUUID().toString())
                .quantityOnHand(3)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .price(new BigDecimal(11.25))
                .version(1)
                .build();
    }
}
