Feature: Register User

  # TC01: Register User - pełna rejestracja i usunięcie konta
  # Gdy email już istnieje, test automatycznie ponawia próbę z unikalnym adresem (retry).

  Scenario Outline: TC01 - Register User
    Given I open the browser
    And I navigate to the home page
    Then I should see the home page
    When I click on "Signup / Login" button
    Then I should see "New User Signup!" section
    When I enter a new user name "<name>" and email "<email>"
    And I click the "Signup" button
    Then I should see "ENTER ACCOUNT INFORMATION" section
    When I fill in the account information
      | password | title | day | month | year |
      | Test123! | Mr    | 15  | 6     | 1990 |
    And I subscribe to the newsletter
    And I agree to receive special offers
    And I fill in the address details
      | firstName | lastName | address    | country | state       | city   | zipcode | mobile    |
      | Jan       | Kowalski | ul. Test 1 | India   | Mazowieckie | Warsaw | 00-001  | 123456789 |
    And I click the "Create Account" button
    Then I should see "ACCOUNT CREATED!" message
    When I click the "Continue" button
    Then I should see that I am logged in
    When I click the "Delete Account" button
    Then I should see "ACCOUNT DELETED!" message
    And I click the "Continue" button

    Examples:
      | name      | email                |
      | Test User | testuser3@example.com |