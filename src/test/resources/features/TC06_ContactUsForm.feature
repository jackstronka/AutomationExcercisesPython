Feature: Contact Us Form

  # TC06: Contact Us Form

  Scenario: TC06 - Contact Us Form
    Given I open the browser
    And I navigate to the home page
    Then I should see the home page
    When I click on "Contact Us" button
    Then I should see "GET IN TOUCH" section
    When I fill in the contact form
      | name    | email              | subject      | message                    |
      | Jan Kowalski | jan@example.com | Test subject | This is a test message. |
    And I upload a file
    And I click the "Submit" button
    And I click the "OK" button
    Then I should see "Success! Your details have been submitted successfully." message
    When I click the "Home" button
    Then I should see the home page
