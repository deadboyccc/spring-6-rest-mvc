package dev.dead.spring6restmvc.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "beer_order")
@Data
@Builder
@NoArgsConstructor
public class BeerOrder {

    @OneToOne(mappedBy = "beerOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private BeerOrderShipment beerOrderShipment;

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

    @Column(name = "customer_ref")
    private String customerRef;

    @ManyToOne(optional = false)
    private Customer customer;

    @OneToMany(mappedBy = "beerOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<BeerOrderLine> beerOrderLines;

    public BeerOrder(UUID id, Long version, LocalDateTime createdDate, LocalDateTime lastModifiedDate,
                     String customerRef,
                     Customer customer, Set<BeerOrderLine> beerOrderLines,
                     BeerOrderShipment beerOrderShipment) {
        this.id = id;
        this.version = version;
        this.createdDate = createdDate;
        this.lastModifiedDate = lastModifiedDate;
        this.customerRef = customerRef;
        this.setCustomer(customer);
        this.beerOrderLines = beerOrderLines;
        this.beerOrderShipment = beerOrderShipment;

    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
        customer.getBeerOrders()
                .add(this);
    }
}
