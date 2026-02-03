package dev.dead.spring6restmvc.events;

import dev.dead.spring6restmvc.entities.Beer;
import lombok.*;
import org.springframework.security.core.Authentication;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BeerCreatedEvent {
    private Beer beer;
    private Authentication authentication;
}
