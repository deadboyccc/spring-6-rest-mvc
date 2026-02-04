package dev.dead.spring6restmvc.listeners;

import dev.dead.spring6restmvc.events.BeerCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BeerCreatedListener {
    @EventListener
    @Async
    void listen(BeerCreatedEvent beerCreatedEvent) {
        log.debug(" Beer Created Event Listener - Beer Id: {}",
                beerCreatedEvent.getBeer()
                        .getId());
    }
}
