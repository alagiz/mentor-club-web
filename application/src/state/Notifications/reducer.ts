import { IMentorRequestStatusNotification, INotificationsState } from "./types";
import {
  CREATE_TASK_UPDATE_NOTIFICATION,
  DELETE_NOTIFICATION,
  SHOW_NOTIFICATION,
  NotificationActionTypes
} from "./actions";

const initialState: INotificationsState = {
  notificationQueue: []
};

export const notificationsReducer = (
  state: INotificationsState = initialState,
  action: NotificationActionTypes
): INotificationsState => {
  switch (action.type) {
    case CREATE_TASK_UPDATE_NOTIFICATION:
      return {
        ...state,
        notificationQueue: [
          ...state.notificationQueue,
          {
            notificationId: action.notificationId,
            visible: action.visible,
            payload: {
              mentorRequestId: action.mentorRequestId,
              newStatus: action.newStatus
            }
          }
        ]
      };
    case SHOW_NOTIFICATION:
      return {
        ...state,
        notificationQueue: state.notificationQueue.map(
          (item: IMentorRequestStatusNotification) =>
            item.notificationId === action.notificationId
              ? {
                  ...item,
                  visible: true
                }
              : item
        )
      };
    case DELETE_NOTIFICATION:
      return {
        ...state,
        notificationQueue: state.notificationQueue.filter(
          (item: IMentorRequestStatusNotification) =>
            item.notificationId !== action.notificationId
        )
      };
    default:
      return state;
  }
};
