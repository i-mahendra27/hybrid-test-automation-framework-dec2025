@ui
@sit @staging @sanity @uat @production @regression
@allure.label.epic:UI-Test
@allure.label.feature:Events
@allure.label.story:SearchNotFound
@allure.label.severity:normal
Feature: Search Events
  As a user
  I want to search events
  So that I can handle no result cases

  Scenario: User search with invalid keyword
    Given I launch the browser
    When I navigate to url "https://eventhub.rahulshettyacademy.com"
    And I enter registered email address and password
    And I click "sign in" button
    And I navigate to "Events" menu
    Then I verify that "Upcoming Events" is visible successfully

    When I enter "XYZ123" in search field
    And I press enter
    Then I should see "No events found" search message
