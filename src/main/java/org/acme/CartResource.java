package org.acme;

import io.smallrye.mutiny.Uni;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.Map;

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

    @PUT
    @Path("{cartId}/{productId}")
    public Uni<Response> addItemToCart(@PathParam("cartId") Long cartId, @PathParam("productId") Long productId) {

        return Cart.addItemToCart(cartId, productId)
                .onItem()
                .transform(c -> Response.ok(c).build());

    }

    @ServerExceptionMapper(value = {UnknownCart.class, UnknownProduct.class})
    public Uni<Response> noSuchCart(Exception e) {

        return Uni.createFrom().item(Response.status(404).entity(Map.of("error", e.getMessage())).build());
    }

}
