package org.acme;

import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import io.smallrye.common.constraint.NotNull;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "cart_item")
public class CartItem extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonbTransient
    public Long id;

    @Column(name = "total_price", precision = 21, scale = 2)
    public BigDecimal totalPrice;

    @Column(name = "quantity")
    public Integer quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    @JsonbTransient
    public Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    public Product product;

}
