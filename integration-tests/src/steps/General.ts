import { Given } from "cypress-cucumber-preprocessor/steps";
import { Server } from "../actors/Server";

Given("the user is authenticated", () => {
  cy.authenticateUser();
});
Given(
  "the {string} endpoint returns an unexpected/authentication error",
  endpoint => {
    Server.stubEndpointWithError(endpoint);
  }
);
Given("the {string} endpoint returns a successful response", endpoint => {
  Server.stubEndpointWithSuccess(endpoint);
});
