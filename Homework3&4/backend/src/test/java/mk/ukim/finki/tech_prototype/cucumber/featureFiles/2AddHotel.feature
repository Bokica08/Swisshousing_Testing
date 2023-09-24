@tag2
Feature: Hotel Management

  Background:
    Given I am logged in as "JDoe" with password "Password@123"
    And I am on the home page and redirected to add hotel page

  Scenario: Add a Hotel
    When I add a hotel with the following details:
      | X    | Y     | Name        | City      | Street       | House Number | Description        | Image Path     | Website                 | Phone Number | Stars |
      | 5    | 5     | HotelName   | CityName  | StreetName   | 123          | Hotel Description  | path/to/image  | www.hotel.com           | 123-456-7890 | 5     |
    Then I should be redirected to the hotels page
    When I select "Name" as the search attribute
    And I enter "HotelName" in the search field
    Then I should see "HotelName" in the search results
