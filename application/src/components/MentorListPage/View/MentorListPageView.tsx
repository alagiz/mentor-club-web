import React from "react";
import { scopedTranslate } from "../../../config/i18next";
import { Styled } from "../Styled";
import { Link } from "react-router-dom";
import { IMentorListPageViewProps } from "./IMentorListPageViewProps";
import { Button, Spin } from "antd";
import Card from "../../Card/Container/Card";
import { includes, keys } from "ramda";

const translateMentorPage = scopedTranslate("mentorListPage");

const MentorListPageView: React.FC<IMentorListPageViewProps> = ({
  mentors,
  isFetchingMentorList,
  handleChooseMentorClick
}) => {
  return (
    <Styled.Container>
      <Styled.TopHeaderContainer>
        <Styled.TopHeader>{translateMentorPage("title")}</Styled.TopHeader>
        <Link to="/mentor-requests/new">
          <Button
            type="ghost"
            style={{
              borderRadius: "0",
              width: "100%",
              marginRight: "5px",
              color: "white"
            }}
            data-id="mentor-list-button"
          >
            {translateMentorPage("newMentorRequest")}
          </Button>
        </Link>
      </Styled.TopHeaderContainer>
      {isFetchingMentorList && (
        <Spin>
          <Styled.SpinnerBoundaries />
        </Spin>
      )}
      {!isFetchingMentorList && mentors && (
        <div data-id="mentor-request-list-table">
          <Styled.MentorRequestList>
            {mentors.map(mentor => (
              <Styled.CardContainer key={mentor.userId}>
                <Card
                  cardTitle={mentor.userName}
                  cardDataItems={keys(mentor)
                    .filter(key => !includes(key, ["userId", "userRole"]))
                    .sort()
                    .reverse()
                    .map(key => ({
                      label: translateMentorPage(`mentorCard.${key}`),
                      valueContainer: {
                        value:
                          key === "userPictureThumbnailSrc" ? (
                            <Styled.UserImage
                              data-id="profile-image"
                              src={mentor.userPictureThumbnailSrc}
                            />
                          ) : (
                            mentor[key]
                          ),
                        customClass:
                          key !== "userPictureThumbnailSrc"
                            ? "card-data-item-text"
                            : ""
                      }
                    }))}
                  cardBodyFooter={
                    <>
                      <Button
                        type="primary"
                        onClick={() => handleChooseMentorClick(mentor.userId)}
                        style={{
                          borderRadius: "0",
                          width: "100%",
                          marginRight: "5px"
                        }}
                      >
                        Choose this mawfawka
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

export default MentorListPageView;
