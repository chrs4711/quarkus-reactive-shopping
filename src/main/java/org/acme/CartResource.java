package org.acme;

import io.smallrye.mutiny.Uni;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.net.URI;

@Path("/carts")
@Produces("application/json")
@Consumes("application/json")
public class CartResource {

    @GET
    public Uni<Response> getCarts() {

        return Cart.getAllCarts().onItem()
                .transform(c -> Response.ok(c).build());
    }

    @GET
    @Path("{id}")
    public Uni<Response> getSingle(Long id) {

        return Cart.getCartById(id)
                .onItem().ifNotNull().transform(i -> Response.ok(i).build())
                .onItem().ifNull().continueWith(Response.ok().status(404).build());
    }

    @POST
    public Uni<Response> createCart(Cart cart) {

        return Cart.createCart(cart)
                .onItem()
                .transform(i -> Response.created(URI.create("/carts/" + i.id)).build());
    }
}
