@tc04 @regression @auth
Feature: Logout User

  # TC04: Logout User - login and logout

  Background:
    Given I open the browser
    And I navigate to the home page
    Then I should see the home page
    And I have a registered user ready for login

  Scenario: TC04 - Logout User
    When I click on "Signup / Login" button
    Then I should see "Login to your account" section
    When I enter email and password from the registered user
    And I click the Login button on login form
    Then I should see that I am logged in
    When I click Logout in header
    Then I should be navigated to the login page
