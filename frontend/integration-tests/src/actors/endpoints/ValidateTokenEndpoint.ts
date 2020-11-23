/// <reference types="Cypress" />

export class ValidateTokenEndpoint {
  public static tokenValid() {
    cy.route({
      method: "GET",
      url: `**/dashboard/use_cases`,
      status: 200,
      response: []
    });
  }
}
