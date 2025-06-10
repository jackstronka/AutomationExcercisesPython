Feature: Contact US form submission

  Scenario Outline: Submit contact form with valid data
    Given the user opens the Contact Us page
    And the user accept cookies if present
    When the user selects "Onboarding" from the team dropdown
    And the user enters name "<name>"
    And the user enters email "<email>"
    And the user selects "Exchange" from the organization dropdown
    And the user enters company name "<company>"
    And the user enters position "<position>"
    And the user selects "$1m-$10m" from the trading volume dropdown
    And the user enters message "<message>"
    And the user submits the form
    Then the form should be submitted or blocked by captcha

    Examples:
      | name        | email                | company     | position   | message             |
      | John Cucumber  | john@example.com     | Acme Corp   | Manager    | Let's talk business |
      | Alice Cucumber | alice@example.org    | Beta Ltd    | Developer  | Interested in B2C2  |
