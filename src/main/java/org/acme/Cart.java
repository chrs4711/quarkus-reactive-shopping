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

    public static Uni<Cart> addItemToCartAsync(Long cartId, Long productId) {

        return Product.findByProductId(productId)
                .onItem().ifNotNull().transform(product -> {

                    return Cart.getCartById(cartId)
                            .onItem().ifNotNull().transform(cart -> {
                                return actuallyAddItemToCart(product, cart);
                            })
                            .onItem().ifNotNull().transformToUni(cart -> {
                               return Panache.withTransaction(cart::persist)
                                       .replaceWith(cart);
                            });

                }).onItem().ifNotNull().transformToUni(item -> item);
        }

    private static Cart actuallyAddItemToCart(Product product, Cart cart) {

        // if item already exists in cart, increment, save and return cart.

        for (CartItem i : cart.cartItems) {
            if (i.product.id == product.id) {
                i.quantity += 1;
                return cart;
            }
        }

        cart.cartItems.add(createItemFor(product));
        return cart;
    }

    //     return Uni.createFrom().completionStage(() ->
        //                     Product.findById(productId))
        //             .onItem().ifNotNull().continueWith(product ->
        //                     Cart.findById(cartId)
        //                             .map(cart -> {
        //                                 // cart.car.add(product);
        //                                 cart.cartItems.add(product);
        //                                 return cart;
        //                             })
        //             )
        //             .map(Cart::persist);
        // }

        // return getCartById(cartId)
        //         .onItem().transformToUni(cart -> {
        //
        //             if (cart==null)
        //                 return Uni.createFrom().nullItem();
        //
        //             System.out.println("cart found");
        //
        //             Uni<Product> productUni = Product.findByProductId(productId)
        //                     .onItem().transformToUni(product -> {
        //                         if (product == null) {
        //                             System.out.println("no product");
        //                             return Uni.createFrom().nullItem();
        //                         }
        //
        //                         System.out.println("product found");
        //                         return Uni.createFrom().item(product);
        //                     });
        //
        //             return Uni.createFrom().item(cart);
        //         });

    private static CartItem createItemFor(Product product) {
        CartItem item = new CartItem();
        item.product = product;
        item.quantity = 1;
        return item;
    }
}
