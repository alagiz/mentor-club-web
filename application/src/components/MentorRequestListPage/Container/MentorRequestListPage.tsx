import React, { useEffect } from "react";
import { connect } from "react-redux";
import { useHistory } from "react-router-dom";
import MentorRequestListPageView from "../View/MentorRequestListPageView";
import { fetchMentorRequestsBegin } from "../../../state/MentorRequests/actions";
import { IAppState } from "../../../store";
import {
  selectFetchMentorRequestsError,
  selectIsFetchingMentorRequests,
  selectMentorRequests
} from "../../../state/MentorRequests/selectors";
import { IMentorRequestListPageProps } from "./IMentorRequestListPageProps";

export const MentorRequestListPage: React.FC<IMentorRequestListPageProps> = ({
  fetchMentorRequestsBegin,
  mentorRequests,
  isLoading,
  error
}) => {
  const history = useHistory();

  useEffect(() => {
    fetchMentorRequestsBegin();
  }, [fetchMentorRequestsBegin]);

  const handleRowClick = (key: string) => {
    history.push(`/mentor-requests/${key}/result`);
  };

  return (
    <MentorRequestListPageView
      mentorRequests={mentorRequests}
      isLoading={isLoading}
      error={error}
      handleRowClick={handleRowClick}
    />
  );
};

export const mapStateToProps = (state: IAppState) => ({
  mentorRequests: selectMentorRequests(state),
  isLoading: selectIsFetchingMentorRequests(state),
  error: selectFetchMentorRequestsError(state)
});

export const mapDispatchToProps = {
  fetchMentorRequestsBegin
} as const;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(MentorRequestListPage);
