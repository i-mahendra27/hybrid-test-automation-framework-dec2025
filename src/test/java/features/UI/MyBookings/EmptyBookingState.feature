@ui
@sit @staging @sanity @uat @production @regression
@allure.label.epic:UI-Test
@allure.label.feature:MyBookings
@allure.label.story:EmptyBookingState
@allure.label.severity:normal
Feature: My Bookings
  As a logged-in user
  I want to manage my event bookings
  So that I can review and track my registered events

  Background:
    Given I launch the browser
    When I navigate to url "https://eventhub.rahulshettyacademy.com/login"
    And I enter registered email address and password
    And I click "sign in" button
    And I navigate to "My Bookings" menu

  Scenario: User has no bookings
    Then I should see the "No bookings yet" message
