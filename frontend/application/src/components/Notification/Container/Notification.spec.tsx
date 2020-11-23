import React from "react";
import { shallow } from "enzyme";
import Notification from "./Notification";
import NotificationView from "../View/NotificationView";
import { Transition } from "react-transition-group";

describe("Given a Notification", () => {
  describe("when rendering", () => {
    const deleteNotificationMock = jest.fn();

    const notification = shallow(
      <Notification deleteNotification={deleteNotificationMock} id={42}>
        Fake Content
      </Notification>
    );

    it("should have Notification matching snapshot", () => {
      expect(notification).toMatchSnapshot();
    });

    it("should dispatch deleteNotification action on handleClose", () => {
      notification
        .find(Transition)
        .dive()
        .find(NotificationView)
        .prop("onClose")();

      expect(deleteNotificationMock).toBeCalledWith(42);
    });
  });
});
