package dev.dead.spring6restmvc.services;

import dev.dead.spring6restmvc.models.Beer;

import java.util.List;
import java.util.UUID;

public interface BeerService {

    Beer getBeerById(UUID id);
    List<Beer> getBeers();
}
