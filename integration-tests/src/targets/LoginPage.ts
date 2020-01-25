/// <reference types="Cypress" />

export class LoginPage {
  public static getErrorMessage = () =>
    cy.get('[data-id="login-error-message"]');

  public static getEmailInput = () => cy.get('[data-id="login-email-input"]');

  public static getPasswordInput = () =>
    cy.get('[data-id="login-password-input"]');

  public static getSubmitButton = () =>
    cy.get('[data-id="login-submit-button"]');
}
