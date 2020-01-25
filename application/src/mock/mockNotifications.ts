import { IMentorRequestStatusNotification } from "../state/Notifications/types";

export const mockNotifications: IMentorRequestStatusNotification[] = [
  {
    notificationId: 1,
    visible: true,
    payload: {
      mentorRequestId: "201",
      newStatus: "DONE"
    }
  },
  {
    notificationId: 2,
    visible: true,
    payload: {
      mentorRequestId: "202",
      newStatus: "DONE"
    }
  },
  {
    notificationId: 3,
    visible: true,
    payload: {
      mentorRequestId: "203",
      newStatus: "DONE"
    }
  }
];
