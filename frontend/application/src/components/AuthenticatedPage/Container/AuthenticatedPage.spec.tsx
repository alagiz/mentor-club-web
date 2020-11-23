import React from "react";
import { shallow } from "enzyme";
import { AuthenticatedPage } from "./AuthenticatedPage";
import { mockNotifications } from "../../../mock/mockNotifications";

describe("Given a AuthenticatedPage", () => {
  describe("when rendering", () => {
    it("should have AuthenticatedPage matching snapshot", () => {
      const authenticatedPage = shallow(
        <AuthenticatedPage
          notifications={mockNotifications}
          deleteNotification={jest.fn()}
          logout={jest.fn()}
        >
          <div />
          <div />
        </AuthenticatedPage>
      );

      expect(authenticatedPage).toMatchSnapshot();
    });
  });
});
