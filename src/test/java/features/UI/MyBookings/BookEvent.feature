@ui
@sit @staging @sanity @uat @production @regression
@allure.label.epic:UI-Test
@allure.label.feature:MyBookings
@allure.label.story:BookEvent
@allure.label.severity:critical
Feature: Book Event
  As a registered user
  I want to book an event
  So that I can reserve tickets and attend the event

  Background:
    Given I launch the browser
    When I navigate to url "https://eventhub.rahulshettyacademy.com/login"
    And I enter registered email address and password
    And I click "sign in" button

  Scenario: User books and event successfully
    When I navigate to "Events" menu
    And I click on any available event card
    And I enter booking information
    And I click "Confirm Booking" button

    Then I should see the "Your tickets are reserved." message

    When I view my bookings
    Then I should be redirected to the my bookings page
    And I verify that "My Bookings" is visible successfully
    And my booked events should be displayed
