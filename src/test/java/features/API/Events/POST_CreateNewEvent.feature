@api
@sit @staging @sanity @uat @production @regression
@allure.label.epic:API-Test
@allure.label.feature:Events
@allure.label.story:CreateEvent
@allure.label.severity:critical
Feature: Create a new event

  Background:
    Given I set "login" API endpoint
    And I set request headers
    And I prepare API base configuration
    And I prepare valid login API payload
    And the login request body should match login request schema
    When I send "POST" request to "login" API
    Then the API response status should be 200
    And the "login" API response should match "success" schema
    And I set "create new event" API endpoint

  Scenario: User successfully create a new event
    Given I prepare create new event API payload
    And the create new event request body should match create new event request schema
    When I send "POST" request to "create new event" API
    Then the API response status should be 201
    And the "create new event" API response should match "success" schema
    And the "create new event" API response message should be "Event created successfully"

  Scenario: User cannot create a new event with invalid payload
    Given I prepare invalid create new event API payload
    When I send "POST" request to "create new event" API
    Then the API response status should be 400
    And the "create new event" API response should match "error" schema
    And the "create new event" API response error should be "Validation failed"
