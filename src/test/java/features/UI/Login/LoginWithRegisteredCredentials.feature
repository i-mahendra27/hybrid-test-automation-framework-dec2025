@ui
@sit @staging @sanity @uat @production @regression
@allure.label.epic:UI-Test
@allure.label.feature:Login
@allure.label.story:ValidLogin
@allure.label.severity:critical
Feature: Login With Registered Credentials
  As a new user
  I want to login with registered account
  So that I can access the application features

  Scenario: User login with valid email and password
    Given I launch the browser
    When I navigate to url "https://eventhub.rahulshettyacademy.com/login"
    Then I verify that "Sign in to EventHub" is visible successfully

    When I enter registered email address and password
    And I click "sign in" button
    Then I verify that "Logged in as user email" is visible
