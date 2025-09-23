package dev.dead.spring6restmvc.controllers;

import dev.dead.spring6restmvc.models.Beer;
import dev.dead.spring6restmvc.services.BeerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/beer")
public class BeerController {
    private final BeerService beerService;

    @GetMapping()
    public List<Beer> getBeers() {
        return beerService.getBeers();
    }


    @GetMapping("/{beerId}")
    public Beer getBeerById(@PathVariable("beerId") UUID beerId) {
        log.info("Get Beer by Id - in Controller. Id: {}", beerId.toString());
        return beerService.getBeerById(beerId);
    }
}
