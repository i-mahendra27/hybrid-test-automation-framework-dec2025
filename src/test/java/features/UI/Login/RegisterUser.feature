@ui
@sit @staging @sanity @uat @production @regression
@allure.label.epic:UI-Test
@allure.label.feature:Login
@allure.label.story:RegisterUser
@allure.label.severity:critical
Feature: User Registration
  As a new user
  I want to register an account
  So that I can access the application features

  Scenario: Register User
    Given I launch the browser
    When I navigate to url "https://eventhub.rahulshettyacademy.com/login"
    Then I verify that "Sign in to EventHub" is visible successfully

    When I click "Register" button
    Then I verify that "Create your account" is visible successfully
    And I fill in the registration form with valid details

    Then I click "Create Account" button
    And I verify that registered user email is visible
