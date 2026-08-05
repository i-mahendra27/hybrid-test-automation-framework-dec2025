@ui
@sit @staging @sanity @uat @production @regression
@allure.label.epic:UI-Test
@allure.label.feature:Events
@allure.label.story:ViewEventDetail
@allure.label.severity:normal
Feature: My Events Page
  As a logged-in user
  I want to view my events
  So that I can manage and review them

  Scenario Outline: User views event detail from My Events
    Given I launch the browser
    When I navigate to url "https://eventhub.rahulshettyacademy.com"
    And I enter registered email address and password
    And I click "sign in" button
    And I navigate to "Events" menu
    Then I verify that "Upcoming Events" is visible successfully

    When I click on any "<eventName>" card
    Then I should be redirected to event detail page
    And I should see complete event information

    Examples:
      | eventName          |
      | Dilli Diwali Mela  |
