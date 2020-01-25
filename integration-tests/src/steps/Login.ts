/// <reference types="Cypress" />
import { When, Then } from "cypress-cucumber-preprocessor/steps";
import { User } from "../actors/User";
import { TheLoginPage } from "../questions/TheLoginPage";
import { Location } from "../questions/Location";

When("the user visits/is on/the login page", () => {
  User.visitsTheLoginPage();
});
When("the user submits the wrong/correct credentials", () => {
  User.submitsCredentials();
});

Then("an error message should be displayed", () => {
  TheLoginPage.errorMessage().should("exist");
});
Then("the user should navigate to the mentor request list page", () => {
  Location.pathName().should(pathName =>
    expect(pathName).to.eq("/mentor_requests")
  );
});
