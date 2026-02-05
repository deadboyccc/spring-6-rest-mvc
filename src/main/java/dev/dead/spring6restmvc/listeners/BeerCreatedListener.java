package dev.dead.spring6restmvc.listeners;

import dev.dead.spring6restmvc.events.BeerCreatedEvent;
import dev.dead.spring6restmvc.mappers.BeerMapper;
import dev.dead.spring6restmvc.repositories.BeerAuditRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class BeerCreatedListener {
    private final BeerAuditRepository beerAuditRepository;
    private final BeerMapper beerMapper;

    @EventListener
    @Async
    void listen(BeerCreatedEvent beerCreatedEvent) {
        val beerAudit = beerMapper.BeerToBeerAudit(beerCreatedEvent.getBeer());
        beerAudit.setAuditEventType("BEER_CREATED");
        // get security context principal name


        val savedBeerAudit = beerAuditRepository.saveAndFlush(beerAudit);
        log.debug("Saved Beer Audit: {}", savedBeerAudit);
    }
}
