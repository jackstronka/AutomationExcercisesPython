@tc05 @regression @auth
Feature: Register User with existing email

  # TC05: Register User with existing email

  Background:
    Given I open the browser
    And I navigate to the home page
    Then I should see the home page
    And I have a user registered with email "testuser@example.com"

  Scenario Outline: TC05 - Register User with existing email
    When I click on "Signup / Login" button
    Then I should see "New User Signup!" section
    When I enter a new user name "<name>" and email "<email>"
    And I click the Signup button on signup form
    Then I should see "Email Address already exist!" message

    Examples:
      | name      | email                |
      | Test User | testuser@example.com |
