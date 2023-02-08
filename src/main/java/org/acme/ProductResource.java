package org.acme;

import io.quarkus.panache.common.Sort;
import io.smallrye.mutiny.Uni;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;

import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.NOT_IMPLEMENTED;

@Path("/products")
public class ProductResource {

    @GET
    public Uni<List<Product>> getProducts() {

        return Product.listAll(Sort.by("createdAt"));
    }

    @GET
    @Path("{id}")
    public Uni<Response> getSingleProduct(Long id) {

        return Product.findByProductId(id)
                .onItem().ifNotNull().transform(p -> Response.ok(p).build())
                .onItem().ifNull().continueWith(Response.ok().status(404).build());

    }

    @POST
    public Uni<Response> addProduct(Product product) {
        return Product.addProduct(product)
                .onItem().transform(p -> URI.create("/products/" + p.id))
                .onItem().transform(uri -> Response.created(uri).build());
    }

    @PUT
    @Path("{id}")
    public Uni<Response> updateProduct(Long id, Product product) {
        return Product.updateProduct(id, product)
                .onItem().ifNotNull()
                .transform(p -> Response.ok(p).build())
                .onItem().ifNull()
                .continueWith(Response.ok().status(404).build());
    }

    @DELETE
    @Path("{id}")
    public Uni<Response> delete(Long id) {

        return Product.deleteProduct(id).onItem().transform( deleted -> {
            return deleted ? Response.ok().build()
                    : Response.serverError().status(NOT_FOUND).build();
        });

    }

}
