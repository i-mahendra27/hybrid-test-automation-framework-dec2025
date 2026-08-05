@ui
@sit @staging @sanity @uat @production @regression
@allure.label.epic:UI-Test
@allure.label.feature:MyBookings
@allure.label.story:CancelBooking
@allure.label.severity:critical
Feature: Cancel Booking
  As a logged-in user
  I want to cancel a booking
  So that I can indicate that I will not attend the event

  Background:
    Given I launch the browser
    When I navigate to url "https://eventhub.rahulshettyacademy.com/login"
    And I enter registered email address and password
    And I click "sign in" button

    And I have an existing booking
    And I navigate to "My Bookings" menu

  Scenario: Cancel booking successfully
    When I click "Cancel Booking" button
    And I click "Yes, cancel it" button
    Then I should see the "Booking cancelled successfully" message
    And no booked event should no longer appear in My Bookings
