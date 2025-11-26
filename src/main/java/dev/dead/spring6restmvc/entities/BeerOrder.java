package dev.dead.spring6restmvc.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "beer_order")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class BeerOrder {
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToOne(mappedBy = "beerOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private BeerOrderShipment beerOrderShipment;
    @ManyToOne(optional = false, cascade = CascadeType.PERSIST)
    private Customer customer;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    @ToString.Include
    @EqualsAndHashCode.Include
    private UUID id;

    @Version
    private Long version;

    @CreationTimestamp
    @Column(name = "created_date")
    @ToString.Include
    @EqualsAndHashCode.Include
    private LocalDateTime createdDate;

    @UpdateTimestamp
    @Column(name = "last_modified_date")
    @ToString.Include
    @EqualsAndHashCode.Include
    private LocalDateTime lastModifiedDate;

    @Column(name = "customer_ref")
    private String customerRef;
    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "beerOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<BeerOrderLine> beerOrderLines = new HashSet<>();
    public BeerOrder(BeerOrderShipment beerOrderShipment, UUID id, Long version, LocalDateTime createdDate,
                     LocalDateTime lastModifiedDate, String customerRef, Customer customer,
                     Set<BeerOrderLine> beerOrderLines) {
        this.setBeerOrderShipment(beerOrderShipment);
        this.id = id;
        this.version = version;
        this.createdDate = createdDate;
        this.lastModifiedDate = lastModifiedDate;
        this.customerRef = customerRef;
        this.setCustomer(customer);
        this.beerOrderLines = beerOrderLines;
    }

    public void setBeerOrderShipment(BeerOrderShipment beerOrderShipment) {
        this.beerOrderShipment = beerOrderShipment;
        beerOrderShipment.setBeerOrder(this);
    }
    public void setCustomer(Customer customer) {
        this.customer = customer;
        customer.getBeerOrders()
                .add(this);
    }
}
