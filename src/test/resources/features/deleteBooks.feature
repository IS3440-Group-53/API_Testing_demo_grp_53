Feature: Delete a book by its ID

  Scenario: User deletes a book by its ID successfully
    Given the API endpoint for deleting a book is "/api/books/1"
    And the user or admin provides valid credentials "user" and "password"
    When the "user" sends a DELETE request
    Then response status code should be 200
    And the response message should confirm deletion

  Scenario: Admin attempts to delete a book but is not permitted
    Given the API endpoint for deleting a book is "/api/books/1"
    And the user or admin provides valid credentials "admin" and "password"
    When the "admin" sends a DELETE request
    Then response status code should be 403
    And the response message should be "User is not permitted"
