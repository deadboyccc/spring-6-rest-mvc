package dev.dead.spring6restmvc.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;


public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "varchar", length = 36, updatable = false, nullable = false)
    private UUID id;
    @Version
    private Integer version;
    private String customerName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
