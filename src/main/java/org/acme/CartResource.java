package org.acme;

import io.smallrye.mutiny.Uni;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/carts")
public class CartResource {

    @GET
    public Uni<Response> getCarts() {

        return Cart.getAllCarts().onItem()
                .transform(c -> Response.ok(c).build());
    }
}
