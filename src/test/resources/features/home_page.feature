Feature: Home Page Accessibility

  Scenario: Verify that the B2C2 homepage is accessible and has a correct title
    Given the user opens the homepage
    Then the page title should be "Home | B2C2"
