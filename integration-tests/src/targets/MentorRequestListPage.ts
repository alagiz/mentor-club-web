/// <reference types="Cypress" />

export class MentorRequestListPage {
  public static getErrorMessage = () =>
    cy.get('[data-id="fetch-mentor-requests-error"]');

  public static getMentorRequestListTable = () =>
    cy.get('[data-id="mentor-request-list-table"]').children();

  public static getNewMentorRequestButton = () =>
    cy.get('[data-id="new-mentor-request-button"]');

  public static getMentorRequestRow = (rowNumber: number) =>
    MentorRequestListPage.getMentorRequestListTable()
      .get("tr")
      .eq(rowNumber);
}
