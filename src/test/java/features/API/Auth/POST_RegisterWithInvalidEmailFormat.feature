@api
@sit @staging @sanity @uat @production @regression
@allure.label.epic:API-Test
@allure.label.feature:Auth
@allure.label.story:InvalidEmailRegistration
@allure.label.severity:normal
Feature: Register with invalid email format

  Background:
    Given I set "register" API endpoint
    And I set request headers
    And I prepare API base configuration

  Scenario: User cannot register with invalid email format via API
    Given I prepare register API payload with invalid email format
    When I send "POST" request to "register" API
    Then the API response status should be 400
    And the "register" API response should match "error" schema
    And the "register" API response error should be "Validation failed"
    And the "register" API response details should contain
      | field | message                   |
      | email | A valid email is required |

  Scenario: User cannot register with invalid email format and password too short via API
    Given I prepare register API payload with invalid email format and password too short
    When I send "POST" request to "register" API
    Then the API response status should be 400
    And the "register" API response should match "error" schema
    And the "register" API response error should be "Validation failed"
    And the "register" API response details should contain
      | field    | message                                |
      | email    | A valid email is required              |
      | password | Password must be at least 6 characters |
