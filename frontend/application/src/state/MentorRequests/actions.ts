import {
  IFetchMentorRequestResultsOverviewRequest,
  IMentorRequest,
  IMentorRequestResult,
  IMentorRequestStatus,
  IUser
} from "./types";

export const FETCH_MENTOR_REQUESTS_BEGIN =
  "mentor-requests/FETCH_MENTOR_REQUESTS_BEGIN";
export const FETCH_MENTOR_REQUESTS_SUCCESS =
  "mentor-requests/FETCH_MENTOR_REQUESTS_SUCCESS";
export const FETCH_MENTOR_REQUESTS_FAILURE =
  "mentor-requests/FETCH_MENTOR_REQUESTS_FAILURE";

export const CREATE_MENTOR_REQUEST_BEGIN =
  "mentor-requests/CREATE_MENTOR_REQUEST_BEGIN";
export const CREATE_MENTOR_REQUEST_SUCCESS =
  "mentor-requests/CREATE_MENTOR_REQUEST_SUCCESS";
export const CREATE_MENTOR_REQUEST_FAILURE =
  "mentor-requests/CREATE_MENTOR_REQUEST_FAILURE";

export const FETCH_MENTOR_REQUESTS_RESULT_OVERVIEW_BEGIN =
  "mentor-requests/FETCH_MENTOR_REQUESTS_RESULT_OVERVIEW_BEGIN";
export const FETCH_MENTOR_REQUESTS_RESULT_OVERVIEW_SUCCESS =
  "mentor-requests/FETCH_MENTOR_REQUESTS_RESULT_OVERVIEW_SUCCESS";
export const FETCH_MENTOR_REQUESTS_RESULT_OVERVIEW_FAILURE =
  "mentor-requests/FETCH_MENTOR_REQUESTS_RESULT_OVERVIEW_FAILURE";

export const FETCH_MENTOR_LIST_BEGIN =
  "mentor-requests/FETCH_MENTOR_LIST_BEGIN";
export const FETCH_MENTOR_LIST_SUCCESS =
  "mentor-requests/FETCH_MENTOR_LIST_SUCCESS";
export const FETCH_MENTOR_LIST_FAILURE =
  "mentor-requests/FETCH_MENTOR_LIST_FAILURE";

export const CLEAR_NEW_MENTOR_REQUEST_ID =
  "mentor-requests/CLEAR_NEW_MENTOR_REQUEST_ID";

export const UPDATE_MENTOR_REQUEST_STATUS =
  "mentor-requests/UPDATE_MENTOR_REQUEST_STATUS";

export const fetchMentorRequestsBegin = () => {
  return {
    type: FETCH_MENTOR_REQUESTS_BEGIN
  } as const;
};

export const fetchMentorRequestsSuccess = (
  mentorRequests: IMentorRequest[]
) => {
  return {
    type: FETCH_MENTOR_REQUESTS_SUCCESS,
    mentorRequests: mentorRequests
  } as const;
};

export const fetchMentorRequestsFailure = (error: string) => {
  return {
    type: FETCH_MENTOR_REQUESTS_FAILURE,
    error
  } as const;
};

export const createMentorRequestBegin = (requestBody: IMentorRequest) => {
  return {
    type: CREATE_MENTOR_REQUEST_BEGIN,
    requestBody
  } as const;
};

export const createMentorRequestSuccess = (mentorRequestId: string) => {
  return {
    type: CREATE_MENTOR_REQUEST_SUCCESS,
    mentorRequestId
  } as const;
};

export const createMentorRequestFailure = (error: string) => {
  return {
    type: CREATE_MENTOR_REQUEST_FAILURE,
    error
  } as const;
};

export const fetchMentorRequestResultOverviewBegin = (
  requestBody: IFetchMentorRequestResultsOverviewRequest
) => {
  return {
    type: FETCH_MENTOR_REQUESTS_RESULT_OVERVIEW_BEGIN,
    requestBody
  } as const;
};

export const fetchMentorRequestResultOverviewSuccess = (
  mentorRequestResult: IMentorRequestResult
) => {
  return {
    type: FETCH_MENTOR_REQUESTS_RESULT_OVERVIEW_SUCCESS,
    mentorRequestResult
  } as const;
};

export const fetchMentorRequestResultOverviewFailure = (error: string) => {
  return {
    type: FETCH_MENTOR_REQUESTS_RESULT_OVERVIEW_FAILURE,
    error
  } as const;
};

export const fetchMentorListBegin = () => {
  return {
    type: FETCH_MENTOR_LIST_BEGIN
  } as const;
};

export const fetchMentorListSuccess = (mentors: IUser[]) => {
  return {
    type: FETCH_MENTOR_LIST_SUCCESS,
    mentors
  } as const;
};

export const fetchMentorListFailure = (error: string) => {
  return {
    type: FETCH_MENTOR_LIST_FAILURE,
    error
  } as const;
};

export const updateMentorRequestStatus = (
  mentorRequestId: string,
  newStatus: IMentorRequestStatus
) => {
  return {
    type: UPDATE_MENTOR_REQUEST_STATUS,
    mentorRequestId,
    newStatus
  } as const;
};

export const clearNewMentorRequestId = () => {
  return {
    type: CLEAR_NEW_MENTOR_REQUEST_ID
  } as const;
};

export type CreateMentorRequestBegin = ReturnType<
  typeof createMentorRequestBegin
>;

export type UpdateMentorRequestStatus = ReturnType<
  typeof updateMentorRequestStatus
>;

export type FetchMentorRequestResultOverview = ReturnType<
  typeof fetchMentorRequestResultOverviewBegin
>;

export type FetchMentorList = ReturnType<typeof fetchMentorListBegin>;

export type MentorRequestsActionTypes = ReturnType<
  | typeof fetchMentorRequestsBegin
  | typeof fetchMentorRequestsSuccess
  | typeof fetchMentorRequestsFailure
  | typeof createMentorRequestBegin
  | typeof createMentorRequestSuccess
  | typeof createMentorRequestFailure
  | typeof fetchMentorRequestResultOverviewBegin
  | typeof fetchMentorRequestResultOverviewSuccess
  | typeof fetchMentorRequestResultOverviewFailure
  | typeof fetchMentorListBegin
  | typeof fetchMentorListSuccess
  | typeof fetchMentorListFailure
  | typeof clearNewMentorRequestId
  | typeof updateMentorRequestStatus
>;
