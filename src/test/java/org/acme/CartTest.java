package org.acme;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.hasItems;

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
}
