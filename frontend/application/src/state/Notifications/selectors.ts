import { Selector } from "../../types/Selector";
import { IMentorRequestStatusNotification } from "./types";

export const selectVisibleNotifications: Selector<IMentorRequestStatusNotification[]> = state =>
  state.notifications.notificationQueue.filter(
    (item: IMentorRequestStatusNotification) => item.visible
  );

export const selectNumberOfVisibleNotifications: Selector<number> = state =>
  selectVisibleNotifications(state).length;

export const selectOldestHiddenNotificationId: Selector<
  number | null
> = state => {
  const hiddenNotification = state.notifications.notificationQueue.find(
    (item: IMentorRequestStatusNotification) => !item.visible
  );

  return hiddenNotification ? hiddenNotification.notificationId : null;
};
