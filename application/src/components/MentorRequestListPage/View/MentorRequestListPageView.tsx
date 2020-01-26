import React from "react";
import translator from "../../../config/i18next";
import { Styled } from "../Styled";
import { Link } from "react-router-dom";
import { IMentorRequestListPageViewProps } from "./IMentorRequestListPageViewProps";
import { Alert, Button, Spin } from "antd";
import Card from "../components/Card/Container/Card";
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
        <Link to="/mentor-requests/new">
          <button data-id="new-mentor-request-button">
            {translator.t("mentorRequestListPage.newMentorRequest")}
          </button>
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
                      value: {
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
