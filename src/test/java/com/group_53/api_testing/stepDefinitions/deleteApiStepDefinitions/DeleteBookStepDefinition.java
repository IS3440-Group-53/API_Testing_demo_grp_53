package com.group_53.api_testing.stepDefinitions.deleteApiStepDefinitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;

public class DeleteBookStepDefinition {

    private Response response;
    private int bookId;

    @Given("the API base URL is {string}")
    public void setBaseUrl(String baseUrl) {
        RestAssured.baseURI = baseUrl;
    }

    @Given("the API endpoint for deleting a book is {string}")
    public void theApiEndpointForDeletingABookIs(String endpoint) {
        // Set the endpoint here, we will use this in the DELETE request
        this.bookId = Integer.parseInt(endpoint.split("/")[3]); // Extracting book ID from the endpoint URL
    }

    @Given("the user provides valid credentials {string} and {string}")
    public void theUserProvidesValidCredentials(String username, String password) {
        // Assuming basic authentication, if needed in your case
        RestAssured.given()
                .auth()
                .basic(username, password); // Set credentials for authentication
    }

    @When("the user sends a DELETE request")
    public void theUserSendsADeleteRequest() {
        String endpoint = "/api/books/" + bookId; // Assuming that bookId is passed dynamically
        RequestSpecification request = RestAssured.given()
                .auth()
                .basic("user", "password")  // Use actual credentials if needed
                .header("Content-Type", "application/json");

        response = request.delete(endpoint); // Send DELETE request
        System.out.println("Response status code: " + response.getStatusCode());
        System.out.println("Response body: " + response.getBody().asString());
    }

    @Then("the response status code should be {int}")
    public void theResponseStatusCodeShouldBe(int expectedStatusCode) {
        int actualStatusCode = response.getStatusCode();
        if (actualStatusCode != expectedStatusCode) {
            Assert.fail("Expected status code " + expectedStatusCode + " but got " + actualStatusCode);
        }
        Assert.assertEquals(actualStatusCode, expectedStatusCode);
    }

    @And("the response message should confirm deletion")
    public void theResponseMessageShouldConfirmDeletion() {
        String responseMessage = response.getBody().asString(); // Get the response message
        // Verify the response contains the expected confirmation message
        Assert.assertTrue(responseMessage.contains("deleted successfully"),
                "The response message does not confirm deletion");
    }

    @And("the response body should contain an error message if the book is not found")
    public void theResponseBodyShouldContainErrorMessageIfBookNotFound() {
        String responseMessage = response.getBody().asString();
        Assert.assertTrue(responseMessage.contains("Book not found"),
                "Error message not found in response");
    }
}