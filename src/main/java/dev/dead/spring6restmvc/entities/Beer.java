package dev.dead.spring6restmvc.entities;

import dev.dead.spring6restmvc.models.BeerStyle;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Beer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    @Version
    private Integer version;
    @NotNull
    @NotBlank
    @Size(max = 50) // validation before db call ( efficient )
    @Column(name = "beer_name", length = 50) // db validation ( less efficient if error bubbled )
    private String beerName;

    @NotNull
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "beer_style")
    private BeerStyle beerStyle;

    @NotNull
    @NotBlank
    @Size(max = 255)
    private String upc;

    @Column(name = "quantity_on_hand")
    private Integer quantityOnHand;

    @NotNull
    private BigDecimal price;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @Builder.Default
    @ManyToMany
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JoinTable(name = "beer_category",
            joinColumns = @JoinColumn(name = "beer_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private Set<Category> categories = new HashSet<>();

    @OneToMany(mappedBy = "beer")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<BeerOrderLine> beerOrderLines;

    public void addCategory(Category category) {
        this.categories.add(category);
        category.getBeers()
                .add(this);
    }

    public void removeCategory(Category category) {
        this.categories.remove(category);
        category.getBeers()
                .remove(this);
    }
}
