export type IMentorRequestStatus =
  | "CREATED"
  | "IN_PROGRESS"
  | "DONE"
  | "FAILED";

export interface IUser {
  userId: string;
  userName: string;
  userRole: string;
  userBio: string;
  userPictureThumbnailSrc: string;
}

export interface IMentorRequest {
  mentorRequestId: string;
  status: IMentorRequestStatus;
  requestDescription: string;
  requesterName: string;
  requesterPictureThumbnailSrc: string;
}

export interface IMentorRequestsState {
  mentors: IUser[] | null;
  mentorRequests: IMentorRequest[] | null;
  mentorRequestResult: IMentorRequestResult | null;
  isFetchingMentorRequests: boolean;
  isFetchingMentorRequestResultOverview: boolean;
  isFetchingMentorList: boolean;
  isCreatingMentorRequest: boolean;
  fetchMentorRequestsError: string | null;
  fetchMentorRequestResultOverviewError: string | null;
  fetchMentorListError: string | null;
  createMentorRequestError: string | null;
  newMentorRequestId: string | null;
}

export interface IFetchMentorRequestResultsOverviewRequest {
  mentorRequestId: string;
}

export interface IFetchMentorRequestResultsOverviewResponse {
  mentorRequestResult: IMentorRequestResult;
}

export interface IMentorRequestResult {
  mentorRequestId: string;
  numberOfLots: number;
  numberOfWafers: number;
  durationInMinutes: number;
  status: IMentorRequestStatus;
  startedAt: string;
  endedAt: string;
}

export interface IFetchMentorRequestResultsDetailRequest {
  mentorRequestId: string;
}

export interface IFetchMentorListResponse {
  mentors: IUser[];
}

export interface IRunMentorRequestRequest {
  userId: string;
  mentorId: string;
  requestDescription: string;
}

export interface IRunMentorRequestResponse {
  mentorRequestId: string;
}
