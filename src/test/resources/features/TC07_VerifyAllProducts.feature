@ignore
Feature: Verify All Products and product detail page

  # TC07: Verify All Products and product detail page

  Scenario: TC07 - Verify All Products and product detail page
    Given I open the browser
    And I navigate to the home page
    Then I should see the home page
    When I click on Products button
    Then I should be navigated to the All Products page
    And the products list is visible
    When I click on "View Product" of the first product
    Then I should be on the product detail page
    And I should see product details: product name, category, price, availability, condition, brand
