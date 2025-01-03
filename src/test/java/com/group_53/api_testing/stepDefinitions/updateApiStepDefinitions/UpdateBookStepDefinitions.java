package com.group_53.api_testing.stepDefinitions.updateApiStepDefinitions;

import io.cucumber.java.en.*;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.example.BaseConfig;
import org.testng.Assert;

public class UpdateBookStepDefinitions {
    private Response response;
    private int bookId;

    public UpdateBookStepDefinitions() {
        RestAssured.baseURI = BaseConfig.BASE_URL;
    }

    @Given("a book exists with id {int}")
    public void aBookExistsWithId(int id) {
        this.bookId = id;
    }

    @When("I send a PUT request to {string} with the following payload")
    public void iSendAPutRequestToWithTheFollowingPayload(String endpoint, String payload) {
        String uniqueSuffix = System.currentTimeMillis() + "_" + (int)(Math.random() * 1000);
        payload = payload.replace("{title}", "UniqueTitle_" + uniqueSuffix)
                .replace("{author}", "UniqueAuthor_" + uniqueSuffix);

        RequestSpecification request = RestAssured.given()
                .auth()
                .basic("admin", "password")
                .header("Content-Type", "application/json")
                .body(payload);
        response = request.put(endpoint);
    }

    @When("I send a PUT request to {string} as a user with the following payload")
    public void sendAPutRequestAsUser(String endpoint, String payload) {
        RequestSpecification request = RestAssured.given()
                .auth()
                .basic("user", "password")
                .header("Content-Type", "application/json")
                .body(payload);
        response = request.put(endpoint);
    }

    @Then("the res status code should be {int}")
    public void verifyStatusCode(int expectedStatusCode) {
        int actualResponse = response.getStatusCode();
        if (actualResponse != expectedStatusCode) {
            Assert.fail("BUG: Expected status code " + expectedStatusCode + " but got " + actualResponse);
        }
        Assert.assertEquals(expectedStatusCode, actualResponse);
    }

    @And("the response body should contain:")
    public void theResponseBodyShouldContain(String expectedResponseBody) {
        try {
            JsonPath jsonPath = response.jsonPath();
            Assert.assertEquals(jsonPath.getInt("id"), bookId);
            Assert.assertEquals(jsonPath.getString("title"), "Jane and Dogs");
            Assert.assertEquals(jsonPath.getString("author"), "John Richard");
        } catch (Exception e) {
            Assert.fail("An error occurred during JSON processing: " + e.getMessage());
        }
    }

    @And("the response body should contain error message:")
    public void verifyErrorResponseBody(String expectedErrorMessage) {
        String actualResponseBody = response.getBody().asString();
        Assert.assertTrue(actualResponseBody.contains(expectedErrorMessage.trim()),
                "Error message not found in response");
    }

    @And("the response body should contain invalid id error message:")
    public void verifyInvalidIdResponseBody(String expectedErrorMessage) {
        String actualResponseBody = response.getBody().asString();
        Assert.assertTrue(actualResponseBody.contains(expectedErrorMessage.trim()),
                "Error message not found in response");
    }

    @And("the response body should contain an authorization error message:")
    public void verifyUnauthorizedAccessResponseBody(String expectedErrorMessage) {
        String actualResponseBody = response.getBody().asString();
        Assert.assertTrue(actualResponseBody.contains(expectedErrorMessage.trim()),
                "Error message not found in response");
    }
}