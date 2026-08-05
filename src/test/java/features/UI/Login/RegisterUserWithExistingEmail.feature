@ui
@sit @staging @sanity @uat @production @regression
@allure.label.epic:UI-Test
@allure.label.feature:Login
@allure.label.story:DuplicateRegistration
@allure.label.severity:normal
Feature: User Registration with Existing Email
  As a registered user
  I want to prevent duplicate account registration
  So that my account remains unique

  Scenario: User cannot register with an existing email
    Given I navigate to url "https://eventhub.rahulshettyacademy.com/login"
    When I verify that "Sign in to EventHub" is visible successfully
    And I already have a registered user
    Then I click "Register" button

    When I verify that "Create your account" is visible successfully
    Then I register with an existing email
    And I click "Create Account" button
    Then I should see an error message "Email already registered"
