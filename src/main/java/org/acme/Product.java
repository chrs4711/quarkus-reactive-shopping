package org.acme;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import io.smallrye.mutiny.Uni;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.json.bind.annotation.JsonbDateFormat;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.ZonedDateTime;

@Entity
public class Product extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public String title;

    public String description;

    @CreationTimestamp
    @JsonbDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
    public ZonedDateTime createdAt;

    @UpdateTimestamp
    @JsonbDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
    public ZonedDateTime updatedAt;

    private static final Logger LOG = LoggerFactory.getLogger(Product.class);

    public static Uni<Product> findByProductId(Long id) {
        return findById(id);
    }

    public static Uni<Product> addProduct(Product product) {
        return Panache
                .withTransaction(product::persist)
                .replaceWith(product);

    }

    public static Uni<Product> updateProduct(Long id, Product product) {

        return Panache.withTransaction(() -> Product.findByProductId(id)
                .onItem().ifNotNull()
                .transform(entity -> {
                    entity.title = product.title;
                    entity.description = product.description;
                    return entity;
                })
                .onFailure().recoverWithNull());

    }

    public static Uni<Boolean> deleteProduct(Long id) {

        LOG.debug("Request to delete product {}", id);

        return Panache.withTransaction(() -> deleteById(id));

    }

}
