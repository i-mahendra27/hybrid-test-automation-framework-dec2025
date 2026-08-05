@api
@sit @staging @sanity @uat @production @regression
@allure.label.epic:API-Test
@allure.label.feature:Bookings
@allure.label.story:GetBookingByReferenceCode
@allure.label.severity:normal
Feature: Get booking by reference code

  Background:
    Given I set "login" API endpoint
    And I set request headers
    And I prepare API base configuration
    And I prepare valid login API payload
    And the login request body should match login request schema
    When I send "POST" request to "login" API
    Then the API response status should be 200
    And the "login" API response should match "success" schema
    And I set "get booking by reference code" API endpoint

  Scenario: Successfully get booking by a valid reference code
    Given I set "create new event" API endpoint
    And I prepare create new event API payload
    And the create new event request body should match create new event request schema
    When I send "POST" request to "create new event" API
    Then the API response status should be 201
    And the "create new event" API response should match "success" schema
    And I store event id from API response
    And I set "create new booking" API endpoint
    And I prepare create new booking API payload for stored event
    And the create new booking request body should match create new booking request schema
    When I send "POST" request to "create new booking" API
    Then the API response status should be 201
    And the "create new booking" API response should match "success" schema
    And I store booking reference code from API response
    And I set "get booking by reference code" API endpoint
    When I send "GET" request to "get booking by reference code" API
    Then the API response status should be 200
    And the "get booking by reference code" API response should match "success" schema

  Scenario: Booking is not found by invalid reference code
    Given I set reference code path parameter to EVT-XYZ123
    When I send "GET" request to "get booking by reference code" API
    Then the API response status should be 404
    And the "get booking by reference code" API response should match "error" schema
    And the "get booking by reference code" API response error should be "Booking with reference \"EVT-XYZ123\" not found"

  Scenario: Resource not found when reference code has invalid format
    Given I set reference code path parameter to 99
    When I send "GET" request to "get booking by reference code" API
    Then the API response status should be 404
    And the "get booking by reference code" API response should match "error" schema
    And the "get booking by reference code" API response error should be "Booking with reference \"99\" not found"
