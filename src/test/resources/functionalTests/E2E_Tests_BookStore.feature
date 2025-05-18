Feature: End to end test for bookstore API
  Description: The purpose of these tests are to cover happy end to end tests.
  Book Store Swagger URL: https://bookstore.toolsqa.com/swagger/#/

  Background: User generates token for authorization
    Given I am an authorized user

  Scenario: Authorized user is able to add book to the reading list
    Given A list of books are available
    When User add a book to reading link
    Then The book is added

  Scenario: Authorized user is able to remove book from the reading list
    Given A list of books are available
    When  User remove a book from reading link
    Then The book is removed