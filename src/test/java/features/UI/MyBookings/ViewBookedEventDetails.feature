@ui
@sit @staging @sanity @uat @production @regression
@allure.label.epic:UI-Test
@allure.label.feature:MyBookings
@allure.label.story:ViewBookingDetail
@allure.label.severity:normal
Feature: View Booking Detail
  As a logged-in user
  I want to view the details of my booking
  So that I can review my reservation information

  Background:
    Given I launch the browser
    When I navigate to url "https://eventhub.rahulshettyacademy.com/login"
    And I enter registered email address and password
    And I click "sign in" button

    And I have an existing booking
    And I navigate to "My Bookings" menu

  Scenario: View booking details successfully
      When I click "View Details" button
      Then I should be redirected to booking detail page
      And I should see the "confirmed" message

  Scenario: Verify Booking Information
    When I click "View Details" button
    Then I should see customer information:
      | Name    |
      | Email   |
      | Phone   |
      | Tickets |

  Scenario: Verify Total Paid
    When I click "View Details" button
    Then the total paid amount should be calculated correctly
