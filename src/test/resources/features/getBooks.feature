Feature: Fetch Book Details
  To ensure the proper functioning of the library system,
  users should be able to retrieve details of books using APIs.

  Scenario: Retrieve a book by its ID
    Given the API endpoint is "/api/books/1"
    When the user sends a GET request
    Then the response status code should be 200
    And the response should contain the book details
    And the "id" in the response should be 1
    And the "title" and "author" should not be empty

  Scenario: Retrieve all books
    Given the API endpoint is "/api/books"
    When the user sends a GET request
    Then the response status code should be 200
    And the response should contain a list of books
    And each book should have non-empty "id", "title", and "author"
