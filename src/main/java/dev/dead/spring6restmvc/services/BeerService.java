package dev.dead.spring6restmvc.services;

import dev.dead.spring6restmvc.models.BeerDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BeerService {

    Optional<BeerDTO> getBeerById(UUID id);

    List<BeerDTO> getBeers();

    BeerDTO saveNewBeer(BeerDTO beerDTO);

    Optional<BeerDTO> updateBeer(UUID beerId, BeerDTO beerDTO);

    void deleteBeerById(UUID beerId);

    void patchBeerById(UUID beerId, BeerDTO beerDTO);
}
