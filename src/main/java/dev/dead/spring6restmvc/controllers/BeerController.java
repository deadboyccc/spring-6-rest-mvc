package dev.dead.spring6restmvc.controllers;

import dev.dead.spring6restmvc.models.Beer;
import dev.dead.spring6restmvc.services.BeerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
@Slf4j
public class BeerController {
    private final BeerService beerService;
    public Beer getBeerById(UUID id) {
        log.info("Get Beer by Id - in Controller. Id: {}", id.toString());
        return beerService.getBeerById(id);
    }
}
