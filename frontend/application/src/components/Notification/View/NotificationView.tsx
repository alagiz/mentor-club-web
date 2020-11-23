import React from "react";
import { faTimes } from "@fortawesome/free-solid-svg-icons";
import { Styled } from "../Styled";
import { INotificationViewProps } from "./INotificationViewProps";

const NotificationView: React.FC<INotificationViewProps> = ({
  children,
  onClose,
  transitionState
}) => {
  return (
    <Styled.NotificationBody transitionState={transitionState}>
      <Styled.CloseNotificationIcon onClick={onClose} icon={faTimes} />
      {children}
    </Styled.NotificationBody>
  );
};

export default NotificationView;
