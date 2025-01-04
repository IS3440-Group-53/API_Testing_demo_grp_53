package org.example;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class TestDataSetup {
    public static void createTestBooks() {
        RestAssured.baseURI = BaseConfig.BASE_URL;

        String[] books = {
                "{\"id\": 1, \"title\": \"Initial Book 1\", \"author\": \"Author 1\"}",
                "{\"id\": 2, \"title\": \"Initial Book 2\", \"author\": \"Author 2\"}"
        };

        for (String book : books) {
            Response response = RestAssured.given()
                    .auth()
                    .basic("admin", "password")
                    .header("Content-Type", "application/json")
                    .body(book)
                    .post("/api/books");

            if (response.getStatusCode() != 201) {
                System.out.println("Failed to create test book: " + response.getBody().asString());
            }
        }
    }
}