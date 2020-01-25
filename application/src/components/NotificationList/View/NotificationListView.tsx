import React from "react";
import Notification from "../../Notification/Container/Notification";
import { Styled } from "../Styled";
import { INotificationListProps } from "../Container/INotificationListProps";
import { TransitionGroup } from "react-transition-group";

export const NotificationListView: React.FC<INotificationListProps> = ({
  children,
  notifications,
  deleteNotification,
  notificationRenderer
}) => {
  const notificationComponents = notifications.map((item: any) => (
    <Notification
      key={item.notificationId}
      id={item.notificationId}
      deleteNotification={deleteNotification}
    >
      {notificationRenderer(item.payload)}
    </Notification>
  ));

  return (
    <>
      <Styled.NotificationList>
        <TransitionGroup>{notificationComponents}</TransitionGroup>
      </Styled.NotificationList>
      {children}
    </>
  );
};

export default NotificationListView;
