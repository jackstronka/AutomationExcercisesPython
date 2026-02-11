@tc06 @regression @contact
Feature: Contact Us Form

  # TC06: Contact Us Form

  Background:
    Given I open the browser
    And I navigate to the home page
    Then I should see the home page

  Scenario: TC06 - Contact Us Form
    When I click on "Contact Us" button
    Then I should see "GET IN TOUCH" section
    When I fill in the contact form
      | name    | email              | subject      | message                    |
      | Jan Kowalski | jan@example.com | Test subject | This is a test message. |
    And I upload a file
    And I click the Submit button on contact form
    And I accept the alert
    Then I should see "Success! Your details have been submitted successfully." message
    When I click the Home button on contact us page
    Then I should see the home page
