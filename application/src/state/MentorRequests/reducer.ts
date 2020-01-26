import {
  CLEAR_NEW_MENTOR_REQUEST_ID,
  CREATE_MENTOR_REQUEST_BEGIN,
  CREATE_MENTOR_REQUEST_FAILURE,
  CREATE_MENTOR_REQUEST_SUCCESS,
  FETCH_MENTOR_REQUESTS_BEGIN,
  FETCH_MENTOR_REQUESTS_FAILURE,
  FETCH_MENTOR_REQUESTS_RESULT_OVERVIEW_BEGIN,
  FETCH_MENTOR_REQUESTS_RESULT_OVERVIEW_FAILURE,
  FETCH_MENTOR_REQUESTS_RESULT_OVERVIEW_SUCCESS,
  FETCH_MENTOR_REQUESTS_RESULT_WAFER_BEGIN,
  FETCH_MENTOR_REQUESTS_RESULT_WAFER_FAILURE,
  FETCH_MENTOR_REQUESTS_RESULT_WAFER_SUCCESS,
  FETCH_MENTOR_REQUESTS_SUCCESS,
  MentorRequestsActionTypes,
  UPDATE_MENTOR_REQUEST_STATUS
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
    case FETCH_MENTOR_REQUESTS_BEGIN:
      return {
        ...state,
        isFetchingMentorRequests: true,
        fetchMentorRequestsError: null,
        mentorRequests: null,
        mentorRequestResult: null,
        mentorRequestResultWafers: {}
      };
    case FETCH_MENTOR_REQUESTS_SUCCESS:
      return {
        ...state,
        isFetchingMentorRequests: false,
        mentorRequests: action.mentorRequests
      };
    case FETCH_MENTOR_REQUESTS_FAILURE:
      return {
        ...state,
        isFetchingMentorRequests: false,
        fetchMentorRequestsError: action.error
      };
    case FETCH_MENTOR_REQUESTS_RESULT_OVERVIEW_BEGIN:
      return {
        ...state,
        isFetchingMentorRequestResultOverview: true,
        fetchMentorRequestResultOverviewError: null,
        mentorRequestResult: null
      };
    case FETCH_MENTOR_REQUESTS_RESULT_OVERVIEW_SUCCESS:
      return {
        ...state,
        isFetchingMentorRequestResultOverview: false,
        mentorRequestResult: action.mentorRequestResult
      };
    case FETCH_MENTOR_REQUESTS_RESULT_OVERVIEW_FAILURE:
      return {
        ...state,
        isFetchingMentorRequestResultOverview: false,
        fetchMentorRequestResultOverviewError: action.error
      };
    case UPDATE_MENTOR_REQUEST_STATUS: {
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
    case FETCH_MENTOR_REQUESTS_RESULT_WAFER_BEGIN:
      return {
        ...state,
        isFetchingMentorRequestResultWafer: true
      };
    case FETCH_MENTOR_REQUESTS_RESULT_WAFER_SUCCESS:
      return {
        ...state,
        isFetchingMentorRequestResultWafer: false,
        mentorRequestResultWafers: {
          ...state.mentorRequestResultWafers,
          [action.wmsId]: action.mentorRequestResultWafer
        }
      };
    case FETCH_MENTOR_REQUESTS_RESULT_WAFER_FAILURE:
      return {
        ...state,
        isFetchingMentorRequestResultWafer: false,
        fetchMentorRequestResultWaferError: action.error
      };
    case CREATE_MENTOR_REQUEST_BEGIN:
      return {
        ...state,
        isCreatingMentorRequest: true,
        createMentorRequestError: null
      };
    case CREATE_MENTOR_REQUEST_SUCCESS:
      return {
        ...state,
        isCreatingMentorRequest: false,
        newMentorRequestId: action.mentorRequestId
      };
    case CREATE_MENTOR_REQUEST_FAILURE:
      return {
        ...state,
        isCreatingMentorRequest: false,
        createMentorRequestError: action.error
      };
    case CLEAR_NEW_MENTOR_REQUEST_ID:
      return {
        ...state,
        newMentorRequestId: null
      };
    default: {
      return state;
    }
  }
};
