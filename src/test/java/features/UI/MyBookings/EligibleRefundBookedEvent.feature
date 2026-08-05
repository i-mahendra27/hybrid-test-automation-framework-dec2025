@ui
@sit @staging @sanity @uat @production @regression
@allure.label.epic:UI-Test
@allure.label.feature:MyBookings
@allure.label.story:RefundEligibility
@allure.label.severity:normal
Feature: Refund Eligibility for Booked Event
  As a logged-in user
  I want to know whether my booking qualifies for a refund
  So that I understand the cancellation policy before cancelling my booking

  Background:
    Given I launch the browser
    When I navigate to url "https://eventhub.rahulshettyacademy.com/login"
    And I enter registered email address and password
    And I click "sign in" button

  Scenario Outline: Verify refund eligibility based on ticket quantity
    Given I have an existing booking with <tickets> ticket(s)
    And I navigate to "My Bookings" menu
    And I click "View Details" button
    Then I should see refund section

    When I click "Check eligibility for refund?" button
    Then I should see the "<refundStatus>" message

    Examples:
      | tickets | refundStatus            |
      | 1       | Eligible for refund     |
      | 2       | Not eligible for refund |
