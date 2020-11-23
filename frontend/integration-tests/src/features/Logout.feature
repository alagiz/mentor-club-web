Feature: Logout;

  In order to protect information
  As a user
  I want to logout of the application

  Scenario: User returns to the login page after logging out successfully from the system
    Given the user is authenticated
    When the user visits the mentor request list page
    And the user hovers over his profile image
    And the user clicks the logout button
    Then the user should navigate to the login page
