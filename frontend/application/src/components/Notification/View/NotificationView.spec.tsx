import React from "react";
import { shallow } from "enzyme";
import NotificationView from "./NotificationView";

describe("Given a NotificationView", () => {
  describe("when rendering", () => {
    it("should have NotificationView matching snapshot", () => {
      const notificationView = shallow(
        <NotificationView onClose={jest.fn()} transitionState={"entered"} />
      );

      expect(notificationView).toMatchSnapshot();
    });
  });
});
