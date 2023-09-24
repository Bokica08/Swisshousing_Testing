@tag4
Feature: Hotel Deletion

  Background:
    Given I am logged in as "JDoe" with password "Password@123" to delete a hotel
    And I am on the home page

  Scenario: Remove a Hotel
    When I navigate to the hotel list page
    And I search for the hotel with the name "HotelEditName"
    And I click on the delete button for that hotel
    And I search for the hotel with the name "HotelEditName" again
    Then the hotel should not be in the list

