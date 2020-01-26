import { IMentorRequestStatus } from "../MentorRequests/types";

export const CREATE_MENTOR_REQUEST_UPDATE_NOTIFICATION =
  "notifications/CREATE_MENTOR_REQUEST_UPDATE_NOTIFICATION";
export const SHOW_NOTIFICATION = "notifications/SHOW_NOTIFICATION";
export const DELETE_NOTIFICATION = "notifications/DELETE_NOTIFICATION";

export const createMentorRequestUpdateNotification = (
  notificationId: number,
  mentorRequestId: string,
  newStatus: IMentorRequestStatus,
  visible: boolean
) =>
  ({
    type: CREATE_MENTOR_REQUEST_UPDATE_NOTIFICATION,
    notificationId,
    mentorRequestId,
    newStatus,
    visible
  } as const);

export const showNotification = (notificationId: number) =>
  ({
    type: SHOW_NOTIFICATION,
    notificationId
  } as const);

export const deleteNotification = (notificationId: number) =>
  ({
    type: DELETE_NOTIFICATION,
    notificationId
  } as const);

export type NotificationActionTypes = ReturnType<
  | typeof createMentorRequestUpdateNotification
  | typeof showNotification
  | typeof deleteNotification
>;
