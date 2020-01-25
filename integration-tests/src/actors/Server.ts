/// <reference types="Cypress" />

import { AuthenticateEndpoint } from "./endpoints/AuthenticateEndpoint";
import { ValidateTokenEndpoint } from "./endpoints/ValidateTokenEndpoint";

export class Server {
  private static start() {
    cy.server();
  }

  public static stubEndpointWithError(endpoint: string) {
    Server.start();

    switch (endpoint) {
      case "authenticate": {
        AuthenticateEndpoint.withWrongCredentials();
        break;
      }
      default: {
        throw `${endpoint} is not supported`;
      }
    }
  }

  public static stubEndpointWithSuccess(endpoint: string) {
    Server.start();

    switch (endpoint) {
      case "authenticate": {
        AuthenticateEndpoint.withCorrectCredentials();
        break;
      }
      case "validate token": {
        ValidateTokenEndpoint.tokenValid();
        break;
      }
      default: {
        throw `${endpoint} is not supported`;
      }
    }
  }
}
