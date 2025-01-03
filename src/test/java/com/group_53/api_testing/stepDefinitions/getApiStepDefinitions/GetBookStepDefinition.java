package com.group_53.api_testing.stepDefinitions.getApiStepDefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.util.List;
import java.util.Map;

public class GetBookStepDefinition {

    private String endpoint;
    private Response response;

    @Given("the API endpoint is {string}")
    public void the_api_endpoint_is(String apiEndpoint) {
        endpoint = "http://localhost:7081" + apiEndpoint;
    }

    @When("the user sends a GET request")
    public void the_user_sends_a_get_request() {
        response = given()
                .auth()
                .basic("admin", "password")
                .when()
                .get(endpoint);
    }

    @Then("the response status code should be {int}")
    public void the_response_status_code_should_be(int statusCode) {
        assertEquals(response.getStatusCode(), statusCode, "Unexpected status code");
    }

    @Then("the response should contain the book details")
    public void the_response_should_contain_the_book_details() {
        assertNotNull(response.getBody(), "Response body is null");
    }

    @Then("the {string} in the response should be {int}")
    public void the_in_the_response_should_be(String key, int expectedValue) {
        int actualValue = response.jsonPath().getInt(key);
        assertEquals(actualValue, expectedValue, "Mismatch in " + key);
    }

    @Then("the {string} and {string} should not be empty")
    public void the_and_should_not_be_empty(String titleKey, String authorKey) {
        String title = response.jsonPath().getString(titleKey);
        String author = response.jsonPath().getString(authorKey);
        assertNotNull(title, "Title is null");
        assertNotNull(author, "Author is null");
        assert !title.isEmpty() : "Title is empty";
        assert !author.isEmpty() : "Author is empty";
    }

    @Then("the response should contain a list of books")
    public void the_response_should_contain_a_list_of_books() {
        List<Map<String, Object>> books = response.jsonPath().getList("$");
        assertNotNull(books, "Books list is null");
        assert !books.isEmpty() : "Books list is empty";
    }

    @Then("each book should have non-empty {string}, {string}, and {string}")
    public void each_book_should_have_non_empty(String idKey, String titleKey, String authorKey) {
        List<Map<String, Object>> books = response.jsonPath().getList("$");

        for (Map<String, Object> book : books) {
            assertNotNull(book.get(idKey), "Book ID is null");
            assertNotNull(book.get(titleKey), "Book Title is null");
            assertNotNull(book.get(authorKey), "Book Author is null");

            assert !book.get(idKey).toString().isEmpty() : "Book ID is empty";
            assert !book.get(titleKey).toString().isEmpty() : "Book Title is empty";
            assert !book.get(authorKey).toString().isEmpty() : "Book Author is empty";
        }
    }
}
