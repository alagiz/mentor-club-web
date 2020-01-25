import React from "react";
import AuthenticatedPageView from "../View/AuthenticatedPageView";
import { IAuthenticatedPageProps } from "./IAuthenticatedPageProps";
import { connect } from "react-redux";
import { IAppState } from "../../../store";
import { selectVisibleNotifications } from "../../../state/Notifications/selectors";
import { deleteNotification } from "../../../state/Notifications/actions";

export const AuthenticatedPage: React.FC<IAuthenticatedPageProps> = ({
  children,
  notifications,
  logout,
  deleteNotification
}) => {
  return (
    <AuthenticatedPageView
      logout={logout}
      notifications={notifications}
      deleteNotification={deleteNotification}
    >
      {children}
    </AuthenticatedPageView>
  );
};

export const mapStateToProps = (state: IAppState) => ({
  notifications: selectVisibleNotifications(state)
});

export const mapDispatchToProps = {
  deleteNotification
};

export default connect(mapStateToProps, mapDispatchToProps)(AuthenticatedPage);
