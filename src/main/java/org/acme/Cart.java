package org.acme;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import io.smallrye.mutiny.Uni;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "cart")
@NamedQueries(value = {
        @NamedQuery(name = "cart.findAll", query = "SELECT c FROM Cart c LEFT JOIN FETCH c.cartItems item LEFT JOIN FETCH item.product"),
        @NamedQuery(name = "cart.findById", query = "SELECT c FROM Cart c LEFT JOIN FETCH c.cartItems item LEFT JOIN FETCH item.product WHERE c.id = ?1")
})
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

    public static Uni<Cart> getCartById(Long id) {
        return find("#cart.findById", id).firstResult();
    }

    public static Uni<Cart> createCart(String name) {

        Cart newCart = new Cart();
        newCart.name = name;

        return Panache.withTransaction(newCart::persist).replaceWith(newCart);

    }

    public static Uni<Cart> createCart(Cart cart) {

        return Panache.withTransaction(cart::persist)
                .replaceWith(cart);

    }

    public static Uni<Cart> addItemToCart(Long cartId, Long productId) {

        return Product.findByProductId(productId)
                .onItem().transformToUni(product -> Cart.getCartById(cartId)
                .onItem().transform(cart -> addItemOrIncrement(product, cart))
                .onItem().transformToUni(cart -> Panache.withTransaction(cart::persist))
                );
    }

    private static Cart addItemOrIncrement(Product product, Cart cart) {

        for (CartItem i : cart.cartItems) {
            if (i.product.id == product.id) {
                i.quantity += 1;
                return cart;
            }
        }

        cart.cartItems.add(createItemFor(product));
        return cart;
    }

    private static CartItem createItemFor(Product product) {
        CartItem item = new CartItem();
        item.product = product;
        item.quantity = 1;
        return item;
    }
}
