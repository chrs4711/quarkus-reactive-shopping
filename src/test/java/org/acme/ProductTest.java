package org.acme;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class ProductTest {

    @Test
    public void testGetProducts() {
        given()
                .when().get("/products")
                .then()
                .statusCode(200)
                .body("findAll{it}.id", hasItems(1, 2, 3, 4));
    }

    @Test
    public void testSingle() {
        given()
                .when().get("/products/1")
                .then()
                .statusCode(200)
                .body("id", is(1))
                .body("title", is("Product1"));
    }

}