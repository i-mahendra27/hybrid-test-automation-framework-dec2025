@api
@sit @staging @sanity @uat @production @regression
@allure.label.epic:API-Test
@allure.label.feature:Bookings
@allure.label.story:CreateBooking
@allure.label.severity:critical
Feature: Create a booking (buy tickets)

  Background:
    Given I set "login" API endpoint
    And I set request headers
    And I prepare API base configuration
    And I prepare valid login API payload
    And the login request body should match login request schema
    When I send "POST" request to "login" API
    Then the API response status should be 200
    And the "login" API response should match "success" schema
    And I set "create new booking" API endpoint

  Scenario: User successfully create booking
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
    And the "create new booking" API response message should be "Booking confirmed!"

  Scenario: User cannot create a new booking with an invalid request payload
    Given I set "create new event" API endpoint
    And I prepare create new event API payload
    And the create new event request body should match create new event request schema
    When I send "POST" request to "create new event" API
    Then the API response status should be 201
    And the "create new event" API response should match "success" schema
    And I store event id from API response
    Given I prepare an invalid create booking API payload
    When I send "POST" request to "create booking" API
    Then the API response status should be 400
    And the "create new booking" API response should match "validation error" schema
    And the response should contain validation error details

  Scenario: User cannot create a booking when the requested seats exceed the available seats
    Given I set "create new event" API endpoint
    And I prepare create new event API payload with limited seats
    And the create new event request body should match create new event request schema
    When I send "POST" request to "create new event" API
    Then the API response status should be 201
    And the "create new event" API response should match "success" schema
    And I store event id from API response
    And I set "create new booking" API endpoint
    And I prepare a create booking API payload with a quantity exceeding the available seats
    When I send "POST" request to "create booking" API
    Then the API response status should be 400
    And the "create new booking" API response should match "insufficient seats error" schema
    And the response should contain an insufficient seats error

  Scenario: User cannot create a booking for a non-existent event
    Given I prepare a create booking API payload with a non-existent event ID
    When I send "POST" request to "create new booking" API
    Then the API response status should be 404
    And the "create new booking" API response should match "not found" schema
    And the "create new booking" API response error should be "Event with id 999999 not found"
