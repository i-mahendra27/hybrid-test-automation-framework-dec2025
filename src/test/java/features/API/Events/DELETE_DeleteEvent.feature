@api
@sit @staging @sanity @uat @production @regression
@allure.label.epic:API-Test
@allure.label.feature:Events
@allure.label.story:DeleteEvent
@allure.label.severity:critical
Feature: Delete event

  Background:
    Given I set "login" API endpoint
    And I set request headers
    And I prepare API base configuration
    And I prepare valid login API payload
    And the login request body should match login request schema
    When I send "POST" request to "login" API
    Then the API response status should be 200
    And the "login" API response should match "success" schema
    And I set "delete event" API endpoint

  Scenario: User successfully delete an event
    Given I prepare create new event API payload
    And the create new event request body should match create new event request schema
    When I send "POST" request to "create new event" API
    Then the API response status should be 201
    And I store event id from API response
    When I send "DELETE" request to "delete event" API
    Then the API response status should be 200
    And the "delete event" API response should match "success" schema
    And the "delete event" API response message should be "Event deleted successfully"

  Scenario: User cannot delete an event that does not exist
    Given I set event id path parameter to 999999
    When I send "DELETE" request to "delete event" API
    Then the API response status should be 404
    And the "delete event" API response should match "error" schema
    And the "delete event" API response error should be "Event with id 999999 not found"
