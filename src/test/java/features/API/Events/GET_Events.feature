@api
@sit @staging @sanity @uat @production @regression
@allure.label.epic:API-Test
@allure.label.feature:Events
@allure.label.story:GetEvents
@allure.label.severity:normal
Feature: Get list all events

  Background:
    Given I set "login" API endpoint
    And I set request headers
    And I prepare API base configuration
    And I prepare valid login API payload
    And the login request body should match login request schema
    When I send "POST" request to "login" API
    Then the API response status should be 200
    And the "login" API response should match "success" schema
    And I set "events" API endpoint

  Scenario Outline: User successfully get paginated list of events
    Given I prepare events API query params
      | category | <category> |
      | city     | <city>     |
      | search   | <search>   |
      | page     | <page>     |
      | limit    | <limit>    |
    When I send "GET" request to "events" API
    Then the API response status should be 200
    And the "events" API response should match "success" schema

    Examples:
      | category   | city      | search | page | limit |
      | Conference | Bangalore | Diwali | 1    | 10    |
