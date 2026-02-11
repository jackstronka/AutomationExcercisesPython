@tc02 @regression @auth @smoke
Feature: Login User

  # TC02: Login User with correct email and password
  # Login-only test – Background creates user as prerequisite (outside scenario).

  Background:
    Given I open the browser
    And I navigate to the home page
    Then I should see the home page
    And I have a registered user ready for login

  Scenario: TC02 - Login User with correct email and password
    When I click on "Signup / Login" button
    Then I should see "Login to your account" section
    When I enter email and password from the registered user
    And I click the Login button on login form
    Then I should see that I am logged in
    When I click the Delete Account button when logged in
    Then I should see "ACCOUNT DELETED!" message
    When I click Continue after account is deleted
