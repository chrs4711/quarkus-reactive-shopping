package org.acme;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.mapper.ObjectMapperType;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;

@QuarkusTest
public class CartTest {

    @Test
    public void testGetCarts() {
        given()
                .when().get("/carts")
                .then()
                .statusCode(200)
                .body("findAll{it}.id", hasItems(1));
    }

    @Test
    public void testGetSingle() {
        given()
                .when().get("/carts/1")
                .then()
                .statusCode(200)
                .body("id", is(1));

    }

    @Test
    public void testCreateCart() {

        given()
                .body(getCart(), ObjectMapperType.JSONB)
                .contentType(ContentType.JSON)
                .when()
                .post("/carts")
                .then()
                .statusCode(201)
                .header("Location", endsWith("2"));

        given()
                .when().get("/carts/2")
                .then()
                .statusCode(200)
                .body("id", is(2))
                .body("name", is("testCart"))
                .body("cartItems.product.find{it}.id", is(1));

    }

    private static Cart getCart() {
        Product p = new Product();
        p.id = 1L;

        CartItem cartItem = new CartItem();
        cartItem.product = p;
        cartItem.quantity = 4;

        Cart c = new Cart();
        c.name = "testCart";
        c.cartItems = Set.of(cartItem);
        return c;
    }

}
