import React from "react";
import NotificationListView from "../View/NotificationListView";
import { INotificationListProps } from "./INotificationListProps";

const NotificationList: React.FC<INotificationListProps> = props => (
  <NotificationListView {...props} />
);

export default NotificationList;
