import { shallow } from "enzyme";
import React from "react";
import AuthenticatedPageView from "./AuthenticatedPageView";

describe("Given a AuthenticatedPageView", () => {
  describe("when rendering", () => {
    it("should have AuthenticatedPageView matching snapshot", () => {
      const authenticatedPageView = shallow(
        <AuthenticatedPageView logout={jest.fn()}>
          <div />
          <div />
        </AuthenticatedPageView>
      );

      expect(authenticatedPageView).toMatchSnapshot();
    });
  });
});
