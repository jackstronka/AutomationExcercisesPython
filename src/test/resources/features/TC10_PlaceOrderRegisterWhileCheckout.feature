@tc10 @regression @checkout @smoke
Feature: Place Order: Register while Checkout

  # TC10: Place Order: Register while Checkout

  Background:
    Given I open the browser
    And I navigate to the home page
    Then I should see the home page

  Scenario Outline: TC14 - Place Order: Register while Checkout
    # Add product to cart
    When I click on View Product for first product on home page
    Then I should be on the product detail page
    When I set product quantity to 1
    And I click Add to cart on product detail page
    And I click View Cart button

    # Cart + Proceed To Checkout
    Then I should see the cart page
    When I click the Proceed To Checkout button

    # Modal with Register / Login on /view_cart
    And I click Register / Login in checkout modal

    # Registration and account creation (reuse TC01)
    Then I should see "New User Signup!" section
    When I enter a new user name "<name>" and email "<email>"
    And I click the Signup button on signup form
    Then I should see "ENTER ACCOUNT INFORMATION" section
    When I fill in the account information
      | password | title | day | month | year |
      | Test123! | Mr    | 15  | 6     | 1990 |
    And I subscribe to the newsletter
    And I agree to receive special offers
    And I fill in the address details
      | firstName | lastName | address    | country | state       | city   | zipcode | mobile    |
      | Jan       | Kowalski | ul. Test 1 | India   | Mazowieckie | Warsaw | 00-001  | 123456789 |
    And I click Create Account on signup form
    Then I should see "ACCOUNT CREATED!" message
    When I click Continue after account is created
    Then I should see that I am logged in

    # Return to cart and checkout
    When I click the "Cart" button
    And I click the Proceed To Checkout button

    # Address Details, Review Order, payment
    Then I should see Address Details and Review Your Order
    When I enter "Please deliver ASAP" in order comment
    And I click Place Order button
    And I enter payment details:
      | nameOnCard   | cardNumber       | cvc | expiryMonth | expiryYear |
      | Jan Kowalski | 4111111111111111 | 123 | 12          | 2030       |
    And I click Pay and Confirm Order button
    Then I should see "Your order has been placed successfully!" message

    # Account deletion
    When I click the Delete Account button when logged in
    Then I should see "ACCOUNT DELETED!" message
    When I click Continue after account is deleted

    Examples:
      | name         | email                     |
      | CheckoutUser | checkout_user@example.com |

