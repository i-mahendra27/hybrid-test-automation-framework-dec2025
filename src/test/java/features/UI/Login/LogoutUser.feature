@ui
@sit @staging @sanity @uat @production @regression
@allure.label.epic:UI-Test
@allure.label.feature:Login
@allure.label.story:LogoutUser
@allure.label.severity:normal
Feature: User Logout
  As a registered user
  I want to log out from my account
  So that I can securely exit the application

  Scenario: User logs out successfully
    Given I launch the browser
    When I navigate to url "https://eventhub.rahulshettyacademy.com/login"
    Then I verify that "Sign in to EventHub" is visible successfully

    When I enter registered email address and password
    And I click "sign in" button

    When I click "Logout" button
    Then I should be redirected to the login page
