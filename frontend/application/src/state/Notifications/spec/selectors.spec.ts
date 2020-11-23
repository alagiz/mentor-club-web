import { IAppState } from "../../../store";
import { mockNotifications } from "../../../mock/mockNotifications";
import { selectVisibleNotifications } from "../selectors";
import { IMentorRequestStatusNotification } from "../types";

const state = {
  notifications: { notificationQueue: mockNotifications }
} as IAppState;

describe("selectVisibleNotifications", () => {
  it("should return the visible notifications", () => {
    expect(selectVisibleNotifications(state)).toEqual(
      mockNotifications.filter(
        (item: IMentorRequestStatusNotification) => item.visible
      )
    );
  });
});
