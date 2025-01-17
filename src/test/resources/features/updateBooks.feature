Feature: Update Book API

  Scenario: Successfully update a book
    Given a book exists with id 2
    When I send a PUT request to "/api/books/2" with the following payload
    """
    {
      "id": 2,
      "title": "Jane and Dogs",
      "author": "John Richard"
    }
    """
    Then the res status code should be 200
    And the response body should contain:
    """
    {
      "id": 1,
      "title": "Jane and Dogs",
      "author": "John Richard"
    }
    """

  Scenario: update a book with invalid payload
    Given a book exists with id 2
    When I send a PUT request to "/api/books/2" with the following payload
    """
    {
      "id": 2,
      "title": null,
      "author": null
    }
    """
    Then the res status code should be 400
    And the response body should contain error message:
    """
    Mandatory parameters should not be null
    """

  Scenario: Update a book with invalid ID
    Given a book exists with id 999
    When I send a PUT request to "/api/books/999" with the following payload
    """
    {
      "id": 999,
      "title": "JaneEye",
      "author": "William"
    }
    """
    Then the res status code should be 404
    And the response body should contain invalid id error message:
    """
    Book not found
    """

  Scenario: Update a book as a user
    Given a book exists with id 2
    When I send a PUT request to "/api/books/2" as a user with the following payload
    """
    {
      "id": 2,
      "title": "JaneDew",
      "author": "John Amith"
    }
    """
    Then the res status code should be 403
    And the response body should contain an authorization error message:
    """
    User is not permitted.
    """

  Scenario: Validate that numbers are not accepted in title and author fields
    Given a book exists with id 2
    When I send a PUT request to "/api/books/2" with the following payload
    """
    {
      "id": 2,
      "title": 1234,
      "author": 5678
    }
    """
    Then the res status code should be 400

  Scenario: validate with empty string for author and title field
    Given a book exists with id 2
    When I send a PUT request to "/api/books/2" with the following payload
    """
    {
      "id": 2,
      "title": "",
      "author": ""
    }
    """
    Then the res status code should be 400