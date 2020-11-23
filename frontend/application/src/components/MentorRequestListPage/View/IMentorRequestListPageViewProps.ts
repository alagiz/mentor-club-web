import { IMentorRequestListPageProps } from "../Container/IMentorRequestListPageProps";

export interface IMentorRequestListPageViewProps {
  mentorRequests: IMentorRequestListPageProps["mentorRequests"];
  error: IMentorRequestListPageProps["error"];
  isLoading: IMentorRequestListPageProps["isLoading"];
}
