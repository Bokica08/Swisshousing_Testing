@tag1
Feature: User Login

  Scenario: Successful Login
    Given I am on the login page
    When I enter username "JDoe" and password "Password@123"
    And I click the login button
    Then I should be logged in with username "JDoe"
