package org.acme;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import java.util.Map;

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

    @Test
    public void testCreate() {
        given()
                .contentType(ContentType.JSON)
                .body(Map.of("title", "foo", "description", "bar"))
                .when().post("/products")
                .then()
                .statusCode(201);
    }

    @Test
    public void testUpdate() {
        given()
                .contentType(ContentType.JSON)
                .body(Map.of("title", "foobar"))
                .when().put("/products/1")
                .then()
                .statusCode(200)
                .body("id", is(1))
                .body("title", is("foobar"));
    }

    @Test
    public void testDelete() {
        given()
                .when().delete("/products/1")
                .then()
                .statusCode(200);

        given()
                .when().get("/product/1")
                .then()
                .statusCode(404);
    }
}