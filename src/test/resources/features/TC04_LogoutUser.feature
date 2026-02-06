Feature: Logout User

  # TC04: Logout User - logowanie i wylogowanie

  Background:
    Given I open the browser
    And I navigate to the home page
    Then I should see the home page
    And I have a registered user ready for login
      | name       | password | title | day | month | year | firstName | lastName | address    | country | state       | city   | zipcode | mobile    |
      | Logout User | Test123! | Mr    | 15  | 6     | 1990 | Jan       | Kowalski | ul. Test 1 | India   | Mazowieckie | Warsaw | 00-001  | 123456789 |

  Scenario: TC04 - Logout User
    When I click on "Signup / Login" button
    Then I should see "Login to your account" section
    When I enter email and password from the registered user
    And I click the "Login" button
    Then I should see that I am logged in
    When I click the "Logout" button
    Then I should be navigated to the login page
