Feature: Login User

  # TC02: Login User with correct email and password
  # Background rejestruje użytkownika przed testem logowania.

  Background:
    Given I open the browser
    And I navigate to the home page
    Then I should see the home page
    And I have a registered user ready for login
      | name       | password | title | day | month | year | firstName | lastName | address    | country | state       | city   | zipcode | mobile    |
      | Login User | Test123! | Mr    | 15  | 6     | 1990 | Jan       | Kowalski | ul. Test 1 | India   | Mazowieckie | Warsaw | 00-001  | 123456789 |

  Scenario: TC02 - Login User with correct email and password
    When I click on "Signup / Login" button
    Then I should see "Login to your account" section
    When I enter email and password from the registered user
    And I click the "Login" button
    Then I should see that I am logged in
    When I click the "Delete Account" button
    Then I should see "ACCOUNT DELETED!" message
    And I click the "Continue" button
