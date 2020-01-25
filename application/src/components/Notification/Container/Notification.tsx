import React from "react";
import NotificationView from "../View/NotificationView";
import { INotificationProps } from "./INotificationProps";
import { Transition } from "react-transition-group";
import { TransitionStatus } from "react-transition-group/Transition";

const transitionTimeout = 300;

const Notification: React.FC<INotificationProps> = ({
  children,
  id,
  deleteNotification,
  ...props
}) => {
  const handleClose = () => {
    deleteNotification(id);
  };

  return (
    <Transition timeout={transitionTimeout} {...props}>
      {(transitionState: TransitionStatus) => (
        <NotificationView
          transitionState={transitionState}
          onClose={handleClose}
        >
          {children}
        </NotificationView>
      )}
    </Transition>
  );
};

export default Notification;
