/// <reference types="Cypress" />
import { When, Then } from "cypress-cucumber-preprocessor/steps";
import { User } from "../actors/User";
import { Location } from "../questions/Location";

When("the user hovers over his profile image", () => {
  User.hoversOverProfileImage();
});
When("the user clicks the logout button", () => {
  User.clicksTheLogoutButton();
});

Then("the user should navigate to the login page", () => {
  Location.pathName().should(pathName => expect(pathName).to.eq("/login"));
});
