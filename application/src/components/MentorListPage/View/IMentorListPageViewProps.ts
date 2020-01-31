import { IMentorListPageProps } from "../Container/IMentorListPageProps";

export interface IMentorListPageViewProps {
  mentors: IMentorListPageProps["mentors"];
  isFetchingMentorList: IMentorListPageProps["isFetchingMentorList"];
  handleChooseMentorClick: (mentorUserId: string) => void;
}
