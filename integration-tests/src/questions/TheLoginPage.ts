/// <reference types="Cypress" />

import { LoginPage } from "../targets/LoginPage";

export class TheLoginPage {
  public static errorMessage() {
    return LoginPage.getErrorMessage();
  }
}
