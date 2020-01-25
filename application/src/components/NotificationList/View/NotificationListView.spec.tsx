import React from "react";
import { shallow } from "enzyme";
import NotificationListView from "./NotificationListView";
import { mockNotifications } from "../../../mock/mockNotifications";

describe("Given a NotificationListView", () => {
  describe("when rendering", () => {
    it("should have NotificationListView matching snapshot", () => {
      const notificationListView = shallow(
        <NotificationListView
          deleteNotification={jest.fn()}
          notifications={mockNotifications}
          notificationRenderer={(payload: any) => <div>{payload}</div>}
        >
          <p>Fake Children</p>
        </NotificationListView>
      );

      expect(notificationListView).toMatchSnapshot();
    });
  });
});
