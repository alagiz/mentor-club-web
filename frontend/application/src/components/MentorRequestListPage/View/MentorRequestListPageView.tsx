import React from "react";
import translator from "../../../config/i18next";
import { Styled } from "../Styled";
import { Link } from "react-router-dom";
import { IMentorRequestListPageViewProps } from "./IMentorRequestListPageViewProps";
import { Alert, Button, Spin } from "antd";
import Card from "../../Card/Container/Card";
import { includes, keys } from "ramda";

const MentorRequestListPageView: React.FC<IMentorRequestListPageViewProps> = ({
  mentorRequests,
  isLoading,
  error
}) => {
  return (
    <Styled.Container>
      <Styled.TopHeaderContainer>
        <Styled.TopHeader>
          {translator.t("mentorRequestListPage.title")}
        </Styled.TopHeader>
        <Link to="/mentor-list">
          <Button
            type="ghost"
            style={{
              borderRadius: "0",
              width: "100%",
              marginRight: "5px",
              color: "white"
            }}
            data-id="new-mentor-request-button"
          >
            {translator.t("mentorRequestListPage.newMentorRequest")}
          </Button>
        </Link>
      </Styled.TopHeaderContainer>

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

      {mentorRequests && (
        <div data-id="mentor-request-list-table">
          <Styled.MentorRequestList>
            {mentorRequests.map(mentorRequest => (
              <Styled.CardContainer key={mentorRequest.mentorRequestId}>
                <Card
                  cardTitle={`${mentorRequest.requesterName}, request #${mentorRequest.mentorRequestId}`}
                  cardDataItems={keys(mentorRequest)
                    .filter(
                      key => !includes(key, ["mentorRequestId", "status"])
                    )
                    .sort()
                    .reverse()
                    .map(key => ({
                      label: translator.t(
                        `mentorRequestListPage.mentorRequest.${key}`
                      ),
                      valueContainer: {
                        value:
                          key === "requesterPictureThumbnailSrc" ? (
                            <Styled.UserImage
                              data-id="profile-image"
                              src={mentorRequest.requesterPictureThumbnailSrc}
                            />
                          ) : (
                            mentorRequest[key]
                          ),
                        customClass:
                          key !== "requesterPictureThumbnailSrc"
                            ? "card-data-item-text"
                            : ""
                      }
                    }))}
                  cardBodyFooter={
                    <>
                      <Button
                        type="primary"
                        style={{
                          borderRadius: "0",
                          width: "100%",
                          marginRight: "5px"
                        }}
                      >
                        Accept
                      </Button>
                      <Button
                        type="danger"
                        style={{ borderRadius: "0", width: "100%" }}
                      >
                        Decline
                      </Button>
                    </>
                  }
                  customClassName={"mentor-request-list-page-card"}
                />
              </Styled.CardContainer>
            ))}
          </Styled.MentorRequestList>
        </div>
      )}
    </Styled.Container>
  );
};

export default MentorRequestListPageView;
