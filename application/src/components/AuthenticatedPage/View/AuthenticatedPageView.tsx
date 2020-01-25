import React from "react";
import { Styled } from "../Styled";
import TopBar from "../components/TopBar/Container/TopBar";
import { IAuthenticatedPageProps } from "../Container/IAuthenticatedPageProps";
import NotificationList from "../../NotificationList/Container/NotificationList";
import translator from "../../../config/i18next";
import { translateMentorRequestStatus } from "../../../service/MentorRequestStatusHelper/MentorRequestStatusHelper";
import { Link } from "react-router-dom";

const AuthenticatedPageView: React.FC<IAuthenticatedPageProps> = ({
  children,
  notifications,
  deleteNotification,
  logout
}) => {
  return (
    <Styled.FlexWrapper>
      <Styled.FlexBox>
        <TopBar logout={logout} />
        <NotificationList
          notifications={notifications}
          deleteNotification={deleteNotification}
          notificationRenderer={(payload: any) => (
            <>
              {translator.t("notifications.mentorRequestUpdated", {
                mentorRequestId: payload.mentorRequestId,
                newStatus: translateMentorRequestStatus(payload.newStatus)
              })}
              <br />
              <Link to={`/mentor-requests/${payload.mentorRequestId}/results`}>
                {translator.t("notifications.viewMentorRequest")}
              </Link>
            </>
          )}
        >
          <Styled.Main>{children}</Styled.Main>
        </NotificationList>
      </Styled.FlexBox>
    </Styled.FlexWrapper>
  );
};

export default AuthenticatedPageView;
