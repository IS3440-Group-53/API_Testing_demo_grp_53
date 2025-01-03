package com.group_53.api_testing.stepDefinitions.updateApiStepDefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;

import static io.restassured.RestAssured.given;

public class AddBookAPISteps {

    private Response response;

    @Given("I am an admin user")
    public void iAmAnAdminUser() {
        RestAssured.baseURI = "http://localhost:7081"; // Base URI for API
    }

    @Given("I am a user")
    public void iAmAUser() {
        RestAssured.baseURI = "http://localhost:7081"; // Base URI for API
    }

    @When("I send a POST request to {string} with the book details as admin")
    public void iSendAPostRequestToWithTheBookDetailsAsAdmin(String apiPath) {
        String requestBody = "{\n" +
                "  \"title\": \"New Book\",\n" +
                "  \"author\": \"Author Name\"\n" +
                "}";

        response = given()
                .auth().basic("admin", "password") // Admin credentials
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post(apiPath);
    }

    @When("I send a POST request to {string} with the book details as user")
    public void iSendAPostRequestToWithTheBookDetailsAsUser(String apiPath) {
        String requestBody = "{\n" +
                "  \"title\": \"New Book1\",\n" +
                "  \"author\": \"Author Name1\"\n" +
                "}";

        response = given()
                .auth().basic("user", "password") // User credentials
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post(apiPath);
    }

    @When("I send a POST request to {string} with an integer as the author")
    public void iSendAPostRequestToWithAnIntegerAsTheAuthor(String apiPath) {
        String requestBody = "{\n" +
                "  \"title\": \"Book With Invalid Author\",\n" +
                "  \"author\": 12345\n" +
                "}";

        response = given()
                .auth().basic("admin", "password") // Admin credentials
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post(apiPath);
    }

    @When("I send a POST request to {string} with the book details and additional attributes")
    public void iSendAPostRequestToWithTheBookDetailsAndAdditionalAttributes(String apiPath) {
        String requestBody = "{\n" +
                "  \"title\": \"New Book\",\n" +
                "  \"author\": \"Author Name\",\n" +
                "  \"extraAttribute\": \"Unexpected Value\"\n" +
                "}";

        response = given()
                .auth().basic("admin", "password")
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post(apiPath);
    }

    @When("I send a POST request to {string} with missing values")
    public void iSendAPostRequestToWithMissingValues(String apiPath) {
        String requestBody = "{\n" +
                "  \"title\": null,\n" +
                "  \"author\": null\n" +
                "}";

        response = given()
                .auth().basic("admin", "password")
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post(apiPath);
    }

    @Then("I should receive a response with status code {int}")
    public void iShouldReceiveAResponseWithStatusCode(int statusCode) {
        response.then().statusCode(statusCode);
    }



    @Then("the response should contain an authorization error message {string}")
    public void theResponseShouldContainAnAuthorizationErrorMessage(String errorMessage) {
        Assert.assertTrue(response.body().asString().contains(errorMessage),
                "Expected authorization error message not found: " + errorMessage);
    }

    @Then("the response should contain an error message {string}")
    public void theResponseShouldContainAnErrorMessage(String errorMessage) {
        Assert.assertTrue(response.body().asString().contains(errorMessage),
                "Expected error message not found: " + errorMessage);
    }
    @Then("the response should contain the book's id")
    public void theResponseShouldContainTheBooksId() {
        Assert.assertNotNull(response.jsonPath().get("id"), "The book ID should be present in the response");
    }

    @Then("the response should contain the message {string}")
    public void theResponseShouldContainTheMessage(String expectedMessage) {
        String actualMessage = response.asString();
        Assert.assertEquals(actualMessage, expectedMessage, "The error message does not match.");
    }
}
