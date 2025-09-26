package dev.dead.spring6restmvc.controllers;

import dev.dead.spring6restmvc.models.Beer;
import dev.dead.spring6restmvc.services.BeerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
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
    private final @NotNull BeerService beerService;

    @PatchMapping("{beerId}")
    public @NotNull ResponseEntity<Beer> patchBeer(@PathVariable("beerId") UUID beerId, @RequestBody Beer beer) {
        log.info("Patching beer with id - Controller: {}", beerId);
        beerService.patchBeerById(beerId, beer);
        return ResponseEntity.noContent()
                .build();
    }

    @DeleteMapping("{beerId}")
    public @NotNull ResponseEntity deleteBeer(@PathVariable("beerId") UUID beerId) {
        log.debug("Delete Beer - Controller id: {}", beerId);
        beerService.deleteBeerById(beerId);
        return ResponseEntity.noContent()
                .build();
    }

    @SuppressWarnings("rawtypes")
    @PutMapping("{beerId}")
    public @NotNull ResponseEntity updateBeer(@PathVariable("beerId") UUID beerId, @RequestBody Beer beer) {
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
    public @NotNull ResponseEntity addBeer(@RequestBody @NotNull Beer beer) {
        log.debug("Add Beer - Controller: {}", beer.getBeerName());
        Beer savedBeer = beerService.saveNewBeer(beer);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/v1/beer/" + savedBeer.getId());
        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    @GetMapping()
    public List<Beer> getBeers() {
        return beerService.getBeers();
    }


    @GetMapping("{beerId}")
    public Beer getBeerById(@PathVariable("beerId") @NotNull UUID beerId) {
        log.debug("Get Beer by Id - in Controller. Id: {}", beerId);
        return beerService.getBeerById(beerId);
    }
}
