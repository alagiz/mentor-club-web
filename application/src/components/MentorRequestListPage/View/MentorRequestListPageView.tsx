import React from "react";
import translator from "../../../config/i18next";
import { Styled } from "../Styled";
import { Link } from "react-router-dom";
import { IMentorRequestListPageViewProps } from "./IMentorRequestListPageViewProps";
import { Alert, Spin } from "antd";

const MentorRequestListPageView: React.FC<IMentorRequestListPageViewProps> = ({
  mentorRequests,
  isLoading,
  error
}) => {
  return (
    <Styled.Container>
      <Styled.TopHeader>
        <h1>{translator.t("mentorRequestListPage.title")}</h1>
        <Link to="/mentor-requests/new">
          <button data-id="new-mentor-request-button">
            {translator.t("mentorRequestListPage.newMentorRequest")}
          </button>
        </Link>
      </Styled.TopHeader>

      {isLoading && (
        <Spin>
          <Styled.SpinnerBoundaries />
        </Spin>
      )}

      {error && (
        <Alert
          data-id="fetch-mentor-requests-error"
          type="error"
          message={error}
        />
      )}

      {mentorRequests && <div data-id="mentor-request-list-table"></div>}
    </Styled.Container>
  );
};

export default MentorRequestListPageView;
