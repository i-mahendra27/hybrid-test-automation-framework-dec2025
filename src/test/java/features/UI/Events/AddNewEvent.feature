@ui
@sit @staging @sanity @uat @production @regression
@allure.label.epic:UI-Test
@allure.label.feature:Events
@allure.label.story:AddNewEvent
@allure.label.severity:critical
Feature: Add New Event
  As an admin
  I want to add new events
  So that users can see new events

  Scenario: Admin adds new event successfully
    Given I launch the browser
    When I navigate to url "https://eventhub.rahulshettyacademy.com/login"
    And I enter registered email address and password
    And I click "sign in" button
    And I navigate to "Events" menu
    Then I verify that "Upcoming Events" is visible successfully

    When I click "Add New Event" button
    Then I should be redirected to event creation page
    When I fill all event information
    And I click "Add Event" button
