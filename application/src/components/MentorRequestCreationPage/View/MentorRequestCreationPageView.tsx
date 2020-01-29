import React from "react";
import translator, { scopedTranslate } from "../../../config/i18next";
import { Styled } from "../Styled";
import { Link } from "react-router-dom";
import { IMentorRequestCreationPageViewProps } from "./IMentorRequestCreationPageViewProps";
import { Button, Spin } from "antd";
import FieldInputView from "../../FieldTextInput/View/FieldInputView";
import FieldDropdown from "../../FieldDropdown/Container/FieldDropdown";
import { includes, isNil, keys } from "ramda";
import Card from "../../Card/Container/Card";

const translateMentorRequest = scopedTranslate(
  "mentorRequestCreationPage.mentorRequest"
);

const MentorRequestCreationPageView: React.FC<IMentorRequestCreationPageViewProps> = ({
  mentors,
  setSelectedMentor,
  selectedMentor,
  mentorRequestDescription,
  updateMentorRequestDescription,
  isLoading,
  createMentorRequest
}) => {
  return (
    <Styled.Container>
      <Styled.TopHeaderContainer>
        <Styled.TopHeader>
          {translator.t("mentorRequestCreationPage.title")}
        </Styled.TopHeader>
        <Link to="/mentor-requests/new">
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
            {translator.t("mentorRequestCreationPage.backToMentorList")}
          </Button>
        </Link>
      </Styled.TopHeaderContainer>

      {isLoading && (
        <Spin>
          <Styled.SpinnerBoundaries />
        </Spin>
      )}

      {!isLoading && (
        <Styled.FormAndButtonContainer>
          <Styled.FormContainer>
            <FieldInputView
              labelText={`${translateMentorRequest("description")}`}
              value={mentorRequestDescription}
              onChange={updateMentorRequestDescription}
              isTextArea={true}
              numberOfLinesForTextArea={4}
            />
            <FieldDropdown
              labelText={`${translateMentorRequest("mentor")}`}
              optionValues={
                isNil(mentors) ? [] : mentors.map(mentor => mentor.userName)
              }
              value={!isNil(selectedMentor) ? selectedMentor.userName : ""}
              onChange={(value: string) => {
                const mentor = isNil(mentors)
                  ? null
                  : mentors.find(mentor => mentor.userName === value);

                if (!isNil(mentor)) {
                  setSelectedMentor(mentor);
                }
              }}
            />
            {!isNil(selectedMentor) && (
              <Styled.CardContainer key={selectedMentor.userId}>
                <Card
                  cardTitle={selectedMentor.userName}
                  cardDataItems={keys(selectedMentor)
                    .filter(key => !includes(key, ["userId", "userRole"]))
                    .sort()
                    .reverse()
                    .map(key => ({
                      label: translateMentorRequest(`mentorProfile.${key}`),
                      valueContainer: {
                        value:
                          key === "userPictureThumbnailSrc" ? (
                            <Styled.UserImage
                              data-id="profile-image"
                              src={selectedMentor.userPictureThumbnailSrc}
                            />
                          ) : (
                            selectedMentor[key]
                          ),
                        customClass:
                          key !== "userPictureThumbnailSrc"
                            ? "card-data-item-text"
                            : ""
                      }
                    }))}
                />
              </Styled.CardContainer>
            )}
          </Styled.FormContainer>
          <Styled.ButtonContainer>
            <Styled.NavigationButton
              type="primary"
              data-id="run-task-button"
              onClick={createMentorRequest}
            >
              {translateMentorRequest("sendRequest")}
            </Styled.NavigationButton>
          </Styled.ButtonContainer>
        </Styled.FormAndButtonContainer>
      )}
    </Styled.Container>
  );
};

export default MentorRequestCreationPageView;
