package dev.dead.spring6restmvc.controllers;

import dev.dead.spring6restmvc.models.BeerDTO;
import dev.dead.spring6restmvc.models.BeerStyle;
import dev.dead.spring6restmvc.services.BeerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
public class BeerController {
    public static final String BEER_BASE_URL = "/api/v1/beer";
    public static final String BEER_ID_URL = BEER_BASE_URL + "/{beerId}";
    private final @NotNull BeerService beerService;

    @PatchMapping(BEER_ID_URL)
    public @NotNull ResponseEntity<BeerDTO> patchBeer(@PathVariable("beerId") UUID beerId,
                                                      @RequestBody BeerDTO beerDTO) {
        log.info("Patching beer with id - Controller: {}", beerId);
        if (!beerService.patchBeerById(beerId, beerDTO)
                .isEmpty()) {
            return ResponseEntity.noContent()
                    .build();
        }
        throw new NotFoundException();
    }

    @DeleteMapping(BEER_ID_URL)
    public @NotNull ResponseEntity deleteBeerById(@PathVariable("beerId") UUID beerId) {
        log.debug("Delete Beer - Controller id: {}", beerId);
        if (!beerService.deleteBeerById(beerId)) {
            throw new NotFoundException();
        }
        return ResponseEntity.noContent()
                .build();
    }

    @SuppressWarnings("rawtypes")
    @PutMapping(BEER_ID_URL)
    public @NotNull ResponseEntity updateBeerById(@PathVariable("beerId") UUID beerId,
                                                  @RequestBody @Validated BeerDTO beerDTO) {
        log.debug("updateBeer beerId={} - Controller", beerId);
        var updatedBeer = beerService.updateBeer(beerId, beerDTO);
        if (updatedBeer.isEmpty()) {
            throw new NotFoundException();
        }
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Location",
                "/api/v1/beer/" + beerId);
        return ResponseEntity
                .noContent()
                .headers(responseHeaders)
                .build();
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @PostMapping(BEER_BASE_URL)
    public @NotNull ResponseEntity addBeer(@RequestBody @Validated @NotNull BeerDTO beerDTO) {
        log.debug("Add Beer - Controller: {}", beerDTO.getBeerName());
        BeerDTO savedBeerDTO = beerService.saveNewBeer(beerDTO);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/v1/beer/" + savedBeerDTO.getId());
        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    @GetMapping(BEER_BASE_URL)
    public Page<BeerDTO> getBeers(@RequestParam(required = false) String beerName,
                                  @RequestParam(required = false) BeerStyle beerStyle,
                                  @RequestParam(required = false) Boolean showInventory,
                                  @RequestParam(required = false) Integer pageNumber,
                                  @RequestParam(required = false) Integer pageSize) {
        log.debug("Get Beers - Controller");
        //---
        // debug log
        if (beerName != null) {
            log.debug(" -| Searching for beer name: {} |- ", beerName);
        } else if (beerStyle != null) {
            log.debug(" -| Searching for beer style: {} |- ", beerStyle);

        } else if (showInventory != null) {
            log.debug(" -| Showing inventory: {} |- ", showInventory);
        }
        // ---
        return beerService.getBeers(beerName, beerStyle, showInventory, pageNumber, pageSize);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Void> handleNotFoundException() {
        log.debug(" -| Not Found handler - Beer Controller - 404 |- ");
        return ResponseEntity.notFound()
                .build();
    }

    @GetMapping(BEER_ID_URL)
    public BeerDTO getBeerById(@PathVariable("beerId") @NotNull UUID beerId) {
        log.debug("Get Beer by Id - in Controller. Id: {}", beerId);

        return beerService.getBeerById(beerId)
                .orElseThrow(NotFoundException::new);
    }
}
