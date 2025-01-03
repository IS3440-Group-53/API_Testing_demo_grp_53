package com.group_53.api_testing.stepDefinitions.updateApiStepDefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;

import static io.restassured.RestAssured.given;

public class AddBookAPISteps {

    private Response response;

    @Step("Setting base URI as an admin user")
    @Given("I am an admin user")
    public void iAmAnAdminUser() {
        RestAssured.baseURI = "http://localhost:7081"; // Base URI for API
    }

    @Step("Setting base URI as a regular user")
    @Given("I am a user")
    public void iAmAUser() {
        RestAssured.baseURI = "http://localhost:7081"; // Base URI for API
    }

    @Step("Sending POST request to {apiPath} with admin credentials")
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

        logResponse(response);
    }

    @Step("Sending POST request to {apiPath} with user credentials")
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

        logResponse(response);
    }

    @Step("Sending POST request to {apiPath} with invalid author format")
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

        logResponse(response);
    }

    @Step("Sending POST request to {apiPath} with extra attributes")
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

        logResponse(response);
    }

    @Step("Sending POST request to {apiPath} with missing values")
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

        logResponse(response);
    }

    @Step("Validating response status code is {statusCode}")
    @Then("I should receive a response with status code {int}")
    public void iShouldReceiveAResponseWithStatusCode(int statusCode) {
        response.then().statusCode(statusCode);
    }

    @Step("Validating response contains authorization error message {errorMessage}")
    @Then("the response should contain an authorization error message {string}")
    public void theResponseShouldContainAnAuthorizationErrorMessage(String errorMessage) {
        Assert.assertTrue(response.body().asString().contains(errorMessage),
                "Expected authorization error message not found: " + errorMessage);
    }

    @Step("Validating response contains error message {errorMessage}")
    @Then("the response should contain an error message {string}")
    public void theResponseShouldContainAnErrorMessage(String errorMessage) {
        Assert.assertTrue(response.body().asString().contains(errorMessage),
                "Expected error message not found: " + errorMessage);
    }

    @Step("Validating response contains the book's ID")
    @Then("the response should contain the book's id")
    public void theResponseShouldContainTheBooksId() {
        Assert.assertNotNull(response.jsonPath().get("id"), "The book ID should be present in the response");
    }

    @Step("Validating response contains the message {expectedMessage}")
    @Then("the response should contain the message {string}")
    public void theResponseShouldContainTheMessage(String expectedMessage) {
        String actualMessage = response.asString();
        Assert.assertEquals(actualMessage, expectedMessage, "The error message does not match.");
    }

    @Attachment(value = "Response Log", type = "text/plain")
    private String logResponse(Response response) {
        return response.prettyPrint();
    }
}
