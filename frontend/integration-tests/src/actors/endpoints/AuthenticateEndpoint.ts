/// <reference types="Cypress" />

export class AuthenticateEndpoint {
  public static withWrongCredentials() {
    cy.route({
      method: "POST",
      url: "**/authenticate",
      status: 401,
      response: []
    });
  }

  public static withCorrectCredentials() {
    cy.route({
      method: "POST",
      url: "**/authenticate",
      status: 200,
      response: "fixture:userTestData"
    });
  }
}
