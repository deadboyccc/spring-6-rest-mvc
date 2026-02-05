package dev.dead.spring6restmvc.repositories;

import dev.dead.spring6restmvc.entities.Beer;
import dev.dead.spring6restmvc.entities.BeerAudit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BeerAuditRepository extends JpaRepository<BeerAudit, UUID> {
}
