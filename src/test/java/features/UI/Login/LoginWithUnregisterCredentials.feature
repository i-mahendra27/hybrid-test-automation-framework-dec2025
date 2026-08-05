@ui
@sit @staging @sanity @uat @production @regression
@allure.label.epic:UI-Test
@allure.label.feature:Login
@allure.label.story:UnregisteredLogin
@allure.label.severity:normal
Feature: Login with Unregister Credentials
  As a new user
  I want to login with registered account with incorrect email and password
  So that I can verify that error message is displayed and user is not able to login to the application

  Scenario: User login with incorrect email and password
    Given I launch the browser
    When I navigate to url "https://eventhub.rahulshettyacademy.com/login"
    Then I verify that "Sign in to EventHub" is visible successfully

    When I enter unregister email address and password
    And I click "sign in" button
    Then I verify error "Invalid email or password" is visible
