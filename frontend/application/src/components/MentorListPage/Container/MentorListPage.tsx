import React, { useEffect } from "react";
import { connect } from "react-redux";
import MentorListPageView from "../View/MentorListPageView";
import { fetchMentorListBegin } from "../../../state/MentorRequests/actions";
import { IAppState } from "../../../store";
import {
  selectIsFetchingMentors,
  selectMentors
} from "../../../state/MentorRequests/selectors";
import { useHistory } from "react-router";
import { IMentorListPageProps } from "./IMentorListPageProps";

export const MentorListPage: React.FC<IMentorListPageProps> = ({
  mentors,
  fetchMentorListBegin,
  isFetchingMentorList
}) => {
  const history = useHistory();

  useEffect(() => {
    fetchMentorListBegin();
  }, [fetchMentorListBegin]);

  const handleChooseMentorClick = (mentorUserId: string) => {
    history.push({
      pathname: `/mentor-requests/new`,
      state: { detail: mentorUserId }
    });
  };

  return (
    <MentorListPageView
      mentors={mentors}
      isFetchingMentorList={isFetchingMentorList}
      handleChooseMentorClick={handleChooseMentorClick}
    />
  );
};

export const mapStateToProps = (state: IAppState) => ({
  mentors: selectMentors(state),
  isFetchingMentorList: selectIsFetchingMentors(state)
});

export const mapDispatchToProps = {
  fetchMentorListBegin
} as const;

export default connect(mapStateToProps, mapDispatchToProps)(MentorListPage);
