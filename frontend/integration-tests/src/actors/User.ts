/// <reference types="Cypress" />

import { LoginPage } from "../targets/LoginPage";
import { TopBar } from "../targets/TopBar";
import { MentorRequestListPage } from "../targets/MentorRequestListPage";

export class User {
  public static visitsTheLoginPage() {
    cy.visit("http://localhost:3000/login");
  }

  public static visitsTheMentorRequestListPage() {
    cy.visit("http://localhost:3000/mentor-requests");
  }

  public static submitsCredentials() {
    LoginPage.getEmailInput().type("fakeUser");
    LoginPage.getPasswordInput().type("fakePassword");
    LoginPage.getSubmitButton().click();
  }

  public static hoversOverProfileImage() {
    TopBar.getProfilePopOver().trigger("mouseover");
  }

  public static clicksTheLogoutButton() {
    TopBar.getLogoutButton().click();
  }

  public static clicksTheNewMentorRequestButton() {
    MentorRequestListPage.getNewMentorRequestButton().click();
  }

  public static clicksOnAMentorRequestInTheMentorRequestListTable() {
    MentorRequestListPage.getMentorRequestRow(2).click();
  }
}
