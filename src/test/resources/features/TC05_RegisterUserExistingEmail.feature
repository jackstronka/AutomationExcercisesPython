@ignore
Feature: Register User with existing email

  # TC05: Register User with existing email

  Scenario Outline: TC05 - Register User with existing email
    Given I open the browser
    And I navigate to the home page
    Then I should see the home page
    When I click on "Signup / Login" button
    Then I should see "New User Signup!" section
    When I enter a new user name "<name>" and email "<email>"
    And I click the "Signup" button
    Then I should see "Email Address already exist!" message

    Examples:
      | name      | email                |
      | Test User | testuser@example.com |
