/// <reference types="Cypress" />

export class TopBar {
  public static getProfilePopOver = () =>
    cy.get('[data-id="top-bar-profile-image"]').parent();

  public static getLogoutButton = () =>
    cy.get('[data-id="top-bar-logout-button"]');
}
