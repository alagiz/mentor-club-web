// ***********************************************
// This example commands.js shows you how to
// create various custom commands and overwrite
// existing commands.
//
// For more comprehensive examples of custom
// commands please read more here:
// https://on.cypress.io/custom-commands
// ***********************************************
//
//
// -- This is a parent command --
// Cypress.Commands.add("login", (email, password) => { ... })
//
//
// -- This is a child command --
// Cypress.Commands.add("drag", { prevSubject: 'element'}, (subject, options) => { ... })
//
//
// -- This is a dual command --
// Cypress.Commands.add("dismiss", { prevSubject: 'optional'}, (subject, options) => { ... })
//
//
// -- This is will overwrite an existing command --
// Cypress.Commands.overwrite("visit", (originalFn, url, options) => { ... })

import { Server } from "../../src/actors/Server";

Cypress.Commands.add("authenticateUser", () => {
  Server.stubEndpointWithSuccess("validate token");

  cy.window().then(window => {
    cy.fixture("../fixtures/userTestData.json").then(userData => {
      window.localStorage.setItem("user/token", userData.token);
      window.localStorage.setItem("user/thumbnail", userData.token);
      window.localStorage.setItem("user/username", userData.username);
      window.localStorage.setItem("user/displayName", userData.displayname);
    });
  });
});
