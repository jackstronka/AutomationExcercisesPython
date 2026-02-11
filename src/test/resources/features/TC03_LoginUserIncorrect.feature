@tc03 @regression @auth
Feature: Login User with incorrect credentials

  # TC03: Login User with incorrect email and password

  Background:
    Given I open the browser
    And I navigate to the home page
    Then I should see the home page

  Scenario: TC03 - Login User with incorrect email and password
    When I click on "Signup / Login" button
    Then I should see "Login to your account" section
    When I enter incorrect email address and password
    And I click the Login button on login form
    Then I should see "Your email or password is incorrect!" message
