@api
@sit @staging @sanity @uat @production @regression
@allure.label.epic:API-Test
@allure.label.feature:Bookings
@allure.label.story:GetBookings
@allure.label.severity:normal
Feature: Get list all bookings

  Background:
    Given I set "login" API endpoint
    And I set request headers
    And I prepare API base configuration
    And I prepare valid login API payload
    And the login request body should match login request schema
    When I send "POST" request to "login" API
    Then the API response status should be 200
    And the "login" API response should match "success" schema
    And I set "bookings" API endpoint

  Scenario Outline: User successfully get paginated list of all bookings
    Given I prepare bookings API query params
      | eventId | <eventId> |
      | status  | <status>  |
      | page    | <page>    |
      | limit   | <limit>   |
    When I send "GET" request to "bookings" API
    Then the API response status should be 200
    And the "bookings" API response should match "success" schema

    Examples:
      | eventId | status    | page | limit |
      | 1       | confirmed | 1    | 10    |
