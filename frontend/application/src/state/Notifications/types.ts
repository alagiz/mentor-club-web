import { IMentorRequestStatus } from "../MentorRequests/types";

export interface IAnyNotification {
  notificationId: number;
  visible: boolean;
  payload: any;
}

export interface IMentorRequestStatusNotification extends IAnyNotification {
  payload: {
    mentorRequestId: string;
    newStatus: IMentorRequestStatus;
  };
}

export interface INotificationsState {
  notificationQueue: IAnyNotification[];
}
