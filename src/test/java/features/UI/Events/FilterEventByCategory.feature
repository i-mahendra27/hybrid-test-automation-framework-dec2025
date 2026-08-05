@ui
@sit @staging @sanity @uat @production @regression
@allure.label.epic:UI-Test
@allure.label.feature:Events
@allure.label.story:FilterByCategory
@allure.label.severity:normal
Feature: Filter Events by Category
  As a user
  I want to filter events by category
  So that I can find relevant events

  Scenario: User filter events by category
    Given I launch the browser
    When I navigate to url "https://eventhub.rahulshettyacademy.com"
    And I enter registered email address and password
    And I click "sign in" button
    And I navigate to "Events" menu
    Then I verify that "Upcoming Events" is visible successfully

    When I select "Concert" from category dropdown
    Then I should see only "Concert" events displayed
