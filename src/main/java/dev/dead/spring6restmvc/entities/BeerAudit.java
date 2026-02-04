package dev.dead.spring6restmvc.entities;

import dev.dead.spring6restmvc.models.BeerStyle;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class BeerAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    @ToString.Include
    @EqualsAndHashCode.Include
    private UUID AuditId;

    @Column(columnDefinition = "uuid")
    private UUID id;

    private Integer version;
    @Size(max = 50) // validation before db call ( efficient )
    @Column(name = "beer_name", length = 50)
    private String beerName;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "beer_style")
    private BeerStyle beerStyle;

    @Size(max = 255)
    private String upc;

    @Column(name = "quantity_on_hand")
    private Integer quantityOnHand;

    private BigDecimal price;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @CreationTimestamp
    private LocalDateTime auditCreatedAt;
    private String principalName;
    private String AuditEventType;
}
