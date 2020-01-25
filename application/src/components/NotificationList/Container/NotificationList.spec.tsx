import React from "react";
import { shallow } from "enzyme";
import NotificationList from "./NotificationList";
import { mockNotifications } from "../../../mock/mockNotifications";

describe("Given a NotificationList", () => {
  describe("when rendering", () => {
    it("should have NotificationListView matching snapshot", () => {
      const notificationList = shallow(
        <NotificationList
          deleteNotification={jest.fn()}
          notifications={mockNotifications}
          notificationRenderer={(payload: any) => <div>{payload}</div>}
        />
      );

      expect(notificationList).toMatchSnapshot();
    });
  });
});
