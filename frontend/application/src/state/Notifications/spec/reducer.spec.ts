import { INotificationsState } from "../types";
import { notificationsReducer } from "../reducer";
import {
  createMentorRequestUpdateNotification,
  deleteNotification,
  showNotification
} from "../actions";
import { mockNotifications } from "../../../mock/mockNotifications";

const mockNotification = mockNotifications[0];

describe("Notification reducer", () => {
  const initialState: INotificationsState = {
    notificationQueue: []
  };

  it("should handle create mentor requestupdate notification", () => {
    const result: INotificationsState = notificationsReducer(
      initialState,
      createMentorRequestUpdateNotification(
        mockNotification.notificationId,
        mockNotification.payload.mentorRequestId,
        mockNotification.payload.newStatus,
        true
      )
    );

    expect(result).toStrictEqual({
      ...initialState,
      notificationQueue: [mockNotification]
    });
  });

  it("should handle show notification", () => {
    const modifiedState: INotificationsState = {
      notificationQueue: mockNotifications
    };

    const result: INotificationsState = notificationsReducer(
      modifiedState,
      showNotification(2)
    );

    mockNotifications[1].visible = true;

    expect(result).toStrictEqual({
      ...initialState,
      notificationQueue: mockNotifications
    });
  });

  it("should handle delete notification", () => {
    const modifiedState: INotificationsState = {
      notificationQueue: mockNotifications
    };

    const result: INotificationsState = notificationsReducer(
      modifiedState,
      deleteNotification(3)
    );

    mockNotifications.pop();

    expect(result).toStrictEqual({
      ...initialState,
      notificationQueue: mockNotifications
    });
  });
});
