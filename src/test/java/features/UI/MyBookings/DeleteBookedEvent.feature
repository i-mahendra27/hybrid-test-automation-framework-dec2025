@ui
@sit @staging @sanity @uat @production @regression
@allure.label.epic:UI-Test
@allure.label.feature:MyBookings
@allure.label.story:DeleteBooking
@allure.label.severity:critical
Feature: Delete Booking
  As a logged-in user
  I want to delete a booking
  So that I can remove bookings that I no longer need

  Background:
    Given I launch the browser
    When I navigate to url "https://eventhub.rahulshettyacademy.com/login"
    And I enter registered email address and password
    And I click "sign in" button

    And I have an existing booking
    And I navigate to "My Bookings" menu

  Scenario: Delete booked event successfully
    Given I note the booked event name
    When I click "Clear all bookings" button
    And I confirm booking deletion
    Then no booked event should no longer appear in My Bookings
