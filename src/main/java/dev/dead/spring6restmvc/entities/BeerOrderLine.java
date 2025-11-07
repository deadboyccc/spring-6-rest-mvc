package dev.dead.spring6restmvc.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "beer_order_line")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BeerOrderLine {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    @Version
    private Long version;

    @CreationTimestamp
    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @UpdateTimestamp
    @Column(name = "last_modified_date")
    private LocalDateTime lastModifiedDate;

    @Column(name = "order_quantity")
    private Integer orderQuantity;

    @Column(name = "quantity_allocated")
    private Integer quantityAllocated;

    @ManyToOne
    @JoinColumn(name = "beer_order_id", nullable = false)
    private BeerOrder beerOrder;

    @ManyToOne
    @JoinColumn(name = "beer_id", nullable = false)
    private Beer beer;
}

