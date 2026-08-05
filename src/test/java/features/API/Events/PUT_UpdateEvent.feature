@api
@sit @staging @sanity @uat @production @regression
@allure.label.epic:API-Test
@allure.label.feature:Events
@allure.label.story:UpdateEvent
@allure.label.severity:critical
Feature: Update event

  Background:
    Given I set "login" API endpoint
    And I set request headers
    And I prepare API base configuration
    And I prepare valid login API payload
    And the login request body should match login request schema
    When I send "POST" request to "login" API
    Then the API response status should be 200
    And the "login" API response should match "success" schema
    And I set "update event" API endpoint

  Scenario: User successfully update an event
    Given I prepare create new event API payload
    And the create new event request body should match create new event request schema
    When I send "POST" request to "create new event" API
    Then the API response status should be 201
    And I store event id from API response
    Given I prepare update event API payload
    And the update event request body should match update event request schema
    When I send "PUT" request to "update event" API
    Then the API response status should be 200
    And the "update event" API response should match "success" schema
    And the "update event" API response message should be "Event updated successfully"

  Scenario: User cannot update an event with invalid payload
    Given I prepare create new event API payload
    When I send "POST" request to "create new event" API
    Then the API response status should be 201
    And I store event id from API response
    Given I prepare invalid update event API payload
    When I send "PUT" request to "update event" API
    Then the API response status should be 400
    And the "update event" API response should match "error" schema
    And the "update event" API response error should be "Validation failed"

  Scenario: User cannot update an event that does not exist
    Given I set event id path parameter to 999999
    And I prepare update event API payload
    When I send "PUT" request to "update event" API
    Then the API response status should be 404
    And the "update event" API response should match "error" schema
    And the "update event" API response error should be "Event with id 999999 not found"
