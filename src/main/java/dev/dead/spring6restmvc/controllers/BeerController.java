package dev.dead.spring6restmvc.controllers;

import dev.dead.spring6restmvc.models.Beer;
import dev.dead.spring6restmvc.services.BeerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/beer")
public class BeerController {
    private final BeerService beerService;

    @SuppressWarnings("rawtypes")
    @PutMapping("{beerId}")
    public ResponseEntity updateBeer(@PathVariable("beerId") UUID beerId, @RequestBody Beer beer) {
        log.debug("updateBeer beerId={} - Controller", beerId);
        beerService.updateBeer(beerId, beer);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Location",
                "/api/v1/beer/" + beerId);
        return ResponseEntity
                .noContent()
                .headers(responseHeaders)
                .build();
    }
    @SuppressWarnings({"unchecked", "rawtypes"})
    @PostMapping
    public ResponseEntity addBeer(@RequestBody Beer beer) {
        log.debug("Add Beer - Controller: {}", beer.getBeerName());
        Beer savedBeer = beerService.saveNewBeer(beer);
        HttpHeaders  headers = new HttpHeaders();
        headers.add("Location", "/api/v1/beer/" + savedBeer.getId());
        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    @GetMapping()
    public List<Beer> getBeers() {
        return beerService.getBeers();
    }


    @GetMapping("{beerId}")
    public Beer getBeerById(@PathVariable("beerId") UUID beerId) {
        log.debug("Get Beer by Id - in Controller. Id: {}", beerId.toString());
        return beerService.getBeerById(beerId);
    }
}
