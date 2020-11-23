Feature: Login;

  In order to access the Mentor club application
  As a user
  I want to login to the application using my email address and password

  Scenario: User sees error when submitting incorrect user credentials
    Given the "authenticate" endpoint returns an authentication error
    When the user visits the login page
    And the user submits the wrong credentials
    Then an error message should be displayed

  Scenario: User is routed to the mentor request list view page after submitting correct credentials
    Given the "authenticate" endpoint returns a successful response
    When the user visits the login page
    And the user submits the correct credentials
    Then the user should navigate to the mentor requestlist page
