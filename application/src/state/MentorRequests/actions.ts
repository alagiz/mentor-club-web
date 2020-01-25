import {
  IFetchMentorRequestResultsDetailRequest,
  IFetchMentorRequestResultsOverviewRequest,
  IRunMentorRequestRequest,
  IMentorRequest,
  IMentorRequestResult,
  IMentorRequestStatus,
  IWaferDetails
} from "./types";

export const FETCH_TASKS_BEGIN = "mentor-requests/FETCH_TASKS_BEGIN";
export const FETCH_TASKS_SUCCESS = "mentor-requests/FETCH_TASKS_SUCCESS";
export const FETCH_TASKS_FAILURE = "mentor-requests/FETCH_TASKS_FAILURE";

export const CREATE_TASK_BEGIN = "mentor-requests/CREATE_TASK_BEGIN";
export const CREATE_TASK_SUCCESS = "mentor-requests/CREATE_TASK_SUCCESS";
export const CREATE_TASK_FAILURE = "mentor-requests/CREATE_TASK_FAILURE";

export const FETCH_TASKS_RESULT_OVERVIEW_BEGIN =
  "mentor-requests/FETCH_TASKS_RESULT_OVERVIEW_BEGIN";
export const FETCH_TASKS_RESULT_OVERVIEW_SUCCESS =
  "mentor-requests/FETCH_TASKS_RESULT_OVERVIEW_SUCCESS";
export const FETCH_TASKS_RESULT_OVERVIEW_FAILURE =
  "mentor-requests/FETCH_TASKS_RESULT_OVERVIEW_FAILURE";

export const FETCH_TASKS_RESULT_WAFER_BEGIN =
  "mentor-requests/FETCH_TASKS_RESULT_WAFER_BEGIN";
export const FETCH_TASKS_RESULT_WAFER_SUCCESS =
  "mentor-requests/FETCH_TASKS_RESULT_WAFER_SUCCESS";
export const FETCH_TASKS_RESULT_WAFER_FAILURE =
  "mentor-requests/FETCH_TASKS_RESULT_WAFER_FAILURE";

export const CLEAR_NEW_TASK_ID = "mentor-requests/CLEAR_NEW_TASK_ID";

export const UPDATE_TASK_STATUS = "mentor-requests/UPDATE_TASK_STATUS";

export const fetchMentorRequestsBegin = () => {
  return {
    type: FETCH_TASKS_BEGIN
  } as const;
};

export const fetchMentorRequestsSuccess = (
  mentorRequests: IMentorRequest[]
) => {
  return {
    type: FETCH_TASKS_SUCCESS,
    mentorRequests: mentorRequests
  } as const;
};

export const fetchMentorRequestsFailure = (error: string) => {
  return {
    type: FETCH_TASKS_FAILURE,
    error
  } as const;
};

export const createMentorRequestBegin = (
  requestBody: IRunMentorRequestRequest
) => {
  return {
    type: CREATE_TASK_BEGIN,
    requestBody
  } as const;
};

export const createMentorRequestSuccess = (mentorRequestId: string) => {
  return {
    type: CREATE_TASK_SUCCESS,
    mentorRequestId
  } as const;
};

export const createMentorRequestFailure = (error: string) => {
  return {
    type: CREATE_TASK_FAILURE,
    error
  } as const;
};

export const fetchMentorRequestResultOverviewBegin = (
  requestBody: IFetchMentorRequestResultsOverviewRequest
) => {
  return {
    type: FETCH_TASKS_RESULT_OVERVIEW_BEGIN,
    requestBody
  } as const;
};

export const fetchMentorRequestResultOverviewSuccess = (
  mentorRequestResult: IMentorRequestResult
) => {
  return {
    type: FETCH_TASKS_RESULT_OVERVIEW_SUCCESS,
    mentorRequestResult
  } as const;
};

export const fetchMentorRequestResultOverviewFailure = (error: string) => {
  return {
    type: FETCH_TASKS_RESULT_OVERVIEW_FAILURE,
    error
  } as const;
};

export const fetchMentorRequestResultWaferBegin = (
  requestBody: IFetchMentorRequestResultsDetailRequest
) => {
  return {
    type: FETCH_TASKS_RESULT_WAFER_BEGIN,
    requestBody
  } as const;
};

export const fetchMentorRequestResultWaferSuccess = (
  wmsId: string,
  mentorRequestResultWafer: IWaferDetails
) => {
  return {
    type: FETCH_TASKS_RESULT_WAFER_SUCCESS,
    wmsId,
    mentorRequestResultWafer
  } as const;
};

export const fetchMentorRequestResultWaferFailure = (error: string) => {
  return {
    type: FETCH_TASKS_RESULT_WAFER_FAILURE,
    error
  } as const;
};

export const updateMentorRequestStatus = (
  mentorRequestId: string,
  newStatus: IMentorRequestStatus
) => {
  return {
    type: UPDATE_TASK_STATUS,
    mentorRequestId,
    newStatus
  } as const;
};

export const clearNewMentorRequestId = () => {
  return {
    type: CLEAR_NEW_TASK_ID
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

export type FetchMentorRequestResultWafer = ReturnType<
  typeof fetchMentorRequestResultWaferBegin
>;

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
  | typeof fetchMentorRequestResultWaferBegin
  | typeof fetchMentorRequestResultWaferSuccess
  | typeof fetchMentorRequestResultWaferFailure
  | typeof clearNewMentorRequestId
  | typeof updateMentorRequestStatus
>;
