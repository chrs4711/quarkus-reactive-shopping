package org.acme;

import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import io.smallrye.mutiny.Uni;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "cart")
@NamedQueries(value = @NamedQuery(name = "cart.findAll", query = "SELECT c FROM Cart c LEFT JOIN FETCH c.cartItems item LEFT JOIN FETCH item.product"))
public class Cart extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Transient
    public int cartTotal;

    public String name;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "cart_id")
    public Set<CartItem> cartItems;

    public static Uni<List<Cart>> getAllCarts() {
        return find("#cart.findAll").list();
    }

}
