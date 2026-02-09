Feature: Search Product

  # TC08: Search Product

  Scenario: TC08 - Search Product
    Given I open the browser
    And I navigate to the home page
    Then I should see the home page
    When I click on Products button
    Then I should be navigated to the All Products page
    When I enter "winter" in search and click search button
    Then I should see "SEARCHED PRODUCTS" section
    And I should see products related to search containing "Winter Top"
