import {
  CLEAR_NEW_TASK_ID,
  CREATE_TASK_BEGIN,
  CREATE_TASK_FAILURE,
  CREATE_TASK_SUCCESS,
  FETCH_TASKS_BEGIN,
  FETCH_TASKS_FAILURE,
  FETCH_TASKS_RESULT_OVERVIEW_BEGIN,
  FETCH_TASKS_RESULT_OVERVIEW_FAILURE,
  FETCH_TASKS_RESULT_OVERVIEW_SUCCESS,
  FETCH_TASKS_RESULT_WAFER_BEGIN,
  FETCH_TASKS_RESULT_WAFER_FAILURE,
  FETCH_TASKS_RESULT_WAFER_SUCCESS,
  FETCH_TASKS_SUCCESS,
  MentorRequestsActionTypes,
  UPDATE_TASK_STATUS
} from "./actions";
import { IMentorRequest, IMentorRequestsState } from "./types";

const initialState: IMentorRequestsState = {
  mentorRequests: null,
  mentorRequestResult: null,
  mentorRequestResultWafers: {},
  isFetchingMentorRequests: false,
  isFetchingMentorRequestResultOverview: false,
  isFetchingMentorRequestResultWafer: false,
  isCreatingMentorRequest: false,
  fetchMentorRequestsError: null,
  fetchMentorRequestResultOverviewError: null,
  fetchMentorRequestResultWaferError: null,
  createMentorRequestError: null,
  newMentorRequestId: null
};

export const mentorRequestsReducer = (
  state: IMentorRequestsState = initialState,
  action: MentorRequestsActionTypes
): IMentorRequestsState => {
  switch (action.type) {
    case FETCH_TASKS_BEGIN:
      return {
        ...state,
        isFetchingMentorRequests: true,
        fetchMentorRequestsError: null,
        mentorRequests: null,
        mentorRequestResult: null,
        mentorRequestResultWafers: {}
      };
    case FETCH_TASKS_SUCCESS:
      return {
        ...state,
        isFetchingMentorRequests: false,
        mentorRequests: action.mentorRequests
      };
    case FETCH_TASKS_FAILURE:
      return {
        ...state,
        isFetchingMentorRequests: false,
        fetchMentorRequestsError: action.error
      };
    case FETCH_TASKS_RESULT_OVERVIEW_BEGIN:
      return {
        ...state,
        isFetchingMentorRequestResultOverview: true,
        fetchMentorRequestResultOverviewError: null,
        mentorRequestResult: null
      };
    case FETCH_TASKS_RESULT_OVERVIEW_SUCCESS:
      return {
        ...state,
        isFetchingMentorRequestResultOverview: false,
        mentorRequestResult: action.mentorRequestResult
      };
    case FETCH_TASKS_RESULT_OVERVIEW_FAILURE:
      return {
        ...state,
        isFetchingMentorRequestResultOverview: false,
        fetchMentorRequestResultOverviewError: action.error
      };
    case UPDATE_TASK_STATUS: {
      if (!state.mentorRequests) {
        return state;
      }

      return {
        ...state,
        mentorRequests: state.mentorRequests.map((item: IMentorRequest) =>
          item.mentorRequestId === action.mentorRequestId
            ? {
                ...item,
                status: action.newStatus
              }
            : item
        )
      };
    }
    case FETCH_TASKS_RESULT_WAFER_BEGIN:
      return {
        ...state,
        isFetchingMentorRequestResultWafer: true
      };
    case FETCH_TASKS_RESULT_WAFER_SUCCESS:
      return {
        ...state,
        isFetchingMentorRequestResultWafer: false,
        mentorRequestResultWafers: {
          ...state.mentorRequestResultWafers,
          [action.wmsId]: action.mentorRequestResultWafer
        }
      };
    case FETCH_TASKS_RESULT_WAFER_FAILURE:
      return {
        ...state,
        isFetchingMentorRequestResultWafer: false,
        fetchMentorRequestResultWaferError: action.error
      };
    case CREATE_TASK_BEGIN:
      return {
        ...state,
        isCreatingMentorRequest: true,
        createMentorRequestError: null
      };
    case CREATE_TASK_SUCCESS:
      return {
        ...state,
        isCreatingMentorRequest: false,
        newMentorRequestId: action.mentorRequestId
      };
    case CREATE_TASK_FAILURE:
      return {
        ...state,
        isCreatingMentorRequest: false,
        createMentorRequestError: action.error
      };
    case CLEAR_NEW_TASK_ID:
      return {
        ...state,
        newMentorRequestId: null
      };
    default: {
      return state;
    }
  }
};
