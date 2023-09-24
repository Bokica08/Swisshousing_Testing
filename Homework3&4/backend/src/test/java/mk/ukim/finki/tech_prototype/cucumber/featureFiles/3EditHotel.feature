@tag3
Feature: Editing a Hotel

  Background:
    Given I am logged in as "JDoe" with password "Password@123" to edit a hotel
    And I am on the home page and redirected to list hotel page

  Scenario: Edit a Hotel
    When I search for the hotel i want to edit with the name "HotelName"
    And I click on the edit button
    And I fill in the hotel name with "HotelEditName"
    Then I am redirected to the list hotel page
    When I search for the edited hotel name "HotelEditName"
    Then I should see the hotel name "HotelEditName" in the list