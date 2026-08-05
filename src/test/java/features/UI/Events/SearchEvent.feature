@ui
@sit @staging @sanity @uat @production @regression
@allure.label.epic:UI-Test
@allure.label.feature:Events
@allure.label.story:SearchEvent
@allure.label.severity:normal
Feature: Search Events
  As a user
  I want to search events
  So that I can find events easily

  Scenario: User search event with valid keyword
    Given I launch the browser
    When I navigate to url "https://eventhub.rahulshettyacademy.com"
    And I enter registered email address and password
    And I click "sign in" button
    And I navigate to "Events" menu
    Then I verify that "Upcoming Events" is visible successfully

    When I enter "Diwali" in search field
    And I press enter
    Then I should see event related to "Diwali"
