package dev.dead.spring6restmvc.services;

import dev.dead.spring6restmvc.models.BeerDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BeerService {

    Optional<BeerDTO> getBeerById(UUID id);

    List<BeerDTO> getBeers(String beerName);

    BeerDTO saveNewBeer(BeerDTO beerDTO);

    Optional<BeerDTO> updateBeer(UUID beerId, BeerDTO beerDTO);

    Boolean deleteBeerById(UUID beerId);

    Optional<BeerDTO> patchBeerById(UUID beerId, BeerDTO beerDTO);
}
