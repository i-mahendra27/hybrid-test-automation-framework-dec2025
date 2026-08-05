@api
@sit @staging @sanity @uat @production @regression
@allure.label.epic:API-Test
@allure.label.feature:Auth
@allure.label.story:RegisterNewUser
@allure.label.severity:critical
Feature: Register a new user

  Background:
    Given I set "register" API endpoint
    And I set request headers
    And I prepare API base configuration

  Scenario: User successfully register an account
    Given I prepare register API payload
    And the register request body should match register request schema
    When I send "POST" request to "register" API
    Then the API response status should be 201
    And the "register" API response should match "success" schema
