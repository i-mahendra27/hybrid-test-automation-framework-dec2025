@api
@sit @staging @sanity @uat @production @regression
@allure.label.epic:API-Test
@allure.label.feature:Events
@allure.label.story:GetEventById
@allure.label.severity:normal
Feature: Get event by ID

  Background:
    Given I set "login" API endpoint
    And I set request headers
    And I prepare API base configuration
    And I prepare valid login API payload
    And the login request body should match login request schema
    When I send "POST" request to "login" API
    Then the API response status should be 200
    And the "login" API response should match "success" schema
    And I set "get event by id" API endpoint

  Scenario: Successfully get an event by a valid ID
    Given I set event id path parameter to 1
    When I send "GET" request to "get event by id" API
    Then the API response status should be 200
    And the "get event by id" API response should match "success" schema

  Scenario: Event is not found by invalid ID
    Given I set event id path parameter to 99
    When I send "GET" request to "get event by id" API
    Then the API response status should be 404
    And the "get event by id" API response should match "error" schema
    And the "get event by id" API response error should be "Event with id 99 not found"
