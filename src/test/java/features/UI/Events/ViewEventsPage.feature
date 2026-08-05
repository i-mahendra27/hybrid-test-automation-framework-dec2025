@ui
@sit @staging @sanity @uat @production @regression
@allure.label.epic:UI-Test
@allure.label.feature:Events
@allure.label.story:ViewEventsPage
@allure.label.severity:normal
Feature: Events Page
  As a logged-in user
  I want to view my events
  So that I can manage and review them

  Scenario: User views My Events page successfully
    Given I launch the browser
    When I navigate to url "https://eventhub.rahulshettyacademy.com"
    And I enter registered email address and password
    And I click "sign in" button
    And I navigate to "Events" menu
    Then I verify that "Upcoming Events" is visible successfully
    And I should see list of events
