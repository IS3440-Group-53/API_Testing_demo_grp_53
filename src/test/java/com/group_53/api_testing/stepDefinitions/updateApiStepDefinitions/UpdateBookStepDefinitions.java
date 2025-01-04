package com.group_53.api_testing.stepDefinitions.updateApiStepDefinitions;

import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.example.BaseConfig;
import org.testng.Assert;
import static com.group_53.api_testing.stepDefinitions.APIClass.response;

public class UpdateBookStepDefinitions {
    private int bookId;

    public UpdateBookStepDefinitions() {
        RestAssured.baseURI = BaseConfig.BASE_URL;
    }

    @Step("Setting API base URL to {baseUrl}")
    @Given("the API base URL is {string}")
    public void setBaseUrl(String baseUrl) {
        RestAssured.baseURI = baseUrl;
    }

    @Step("Verifying a book exists with ID {id}")
    @Given("a book exists with id {int}")
    public void aBookExistsWithId(int id) {
        this.bookId = id;
    }

    @Step("Sending PUT request to {endpoint} with admin credentials and payload")
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
        logResponse(response);
    }

    @Step("Sending PUT request to {endpoint} with user credentials and payload")
    @When("I send a PUT request to {string} as a user with the following payload")
    public void sendAPutRequestAsUser(String endpoint, String payload) {
        RequestSpecification request = RestAssured.given()
                .auth()
                .basic("user", "password")
                .header("Content-Type", "application/json")
                .body(payload);

        response = request.put(endpoint);
        logResponse(response);
    }

    @Step("Validating response status code is {expectedStatusCode}")
    @Then("the res status code should be {int}")
    public void verifyStatusCode(int expectedStatusCode) {
        int actualResponse = response.getStatusCode();
        if (actualResponse != expectedStatusCode) {
            Assert.fail("BUG: Expected status code " + expectedStatusCode + " but got " + actualResponse);
        }
        Assert.assertEquals(expectedStatusCode, actualResponse);
    }

    @Step("Validating response body contains book details")
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

    @Step("Validating response body contains error message {expectedErrorMessage}")
    @And("the response body should contain error message:")
    public void verifyErrorResponseBody(String expectedErrorMessage) {
        String actualResponseBody = response.getBody().asString();
        Assert.assertTrue(actualResponseBody.contains(expectedErrorMessage.trim()),
                "Error message not found in response");
    }

    @Step("Validating response body contains invalid ID error message {expectedErrorMessage}")
    @And("the response body should contain invalid id error message:")
    public void verifyInvalidIdResponseBody(String expectedErrorMessage) {
        String actualResponseBody = response.getBody().asString();
        Assert.assertTrue(actualResponseBody.contains(expectedErrorMessage.trim()),
                "Error message not found in response");
    }

    @Step("Validating response body contains authorization error message {expectedErrorMessage}")
    @And("the response body should contain an authorization error message:")
    public void verifyUnauthorizedAccessResponseBody(String expectedErrorMessage) {
        String actualResponseBody = response.getBody().asString();
        logResponse(response); // Log the response for Allure reports
        Assert.assertTrue(actualResponseBody.contains(expectedErrorMessage.trim()),
                "Error message not found in response");
    }

    @Attachment(value = "Response Log", type = "text/plain")
    private String logResponse(Response response) {
        return response.prettyPrint();
    }
}