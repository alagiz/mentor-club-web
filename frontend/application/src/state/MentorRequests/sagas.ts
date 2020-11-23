import { call, put, takeLatest } from "@redux-saga/core/effects";
import { get, post } from "service/Api/Api";
import {
  CREATE_MENTOR_REQUEST_BEGIN,
  CreateMentorRequestBegin,
  createMentorRequestFailure,
  createMentorRequestSuccess,
  FETCH_MENTOR_LIST_BEGIN,
  FETCH_MENTOR_REQUESTS_BEGIN,
  FETCH_MENTOR_REQUESTS_RESULT_OVERVIEW_BEGIN,
  fetchMentorListFailure,
  fetchMentorListSuccess,
  FetchMentorRequestResultOverview,
  fetchMentorRequestResultOverviewFailure,
  fetchMentorRequestResultOverviewSuccess,
  fetchMentorRequestsFailure,
  fetchMentorRequestsSuccess
} from "./actions";
import {
  IFetchMentorRequestResultsOverviewResponse,
  IMentorRequest,
  IRunMentorRequestResponse,
  IUser
} from "./types";

export function* fetchMentorRequests() {
  try {
    const response: IMentorRequest[] = yield call(get, "/mentor-requests");

    yield put(fetchMentorRequestsSuccess(response));
  } catch (error) {
    yield put(fetchMentorRequestsFailure(error.message));
  }
}

export function* createMentorRequest(action: CreateMentorRequestBegin) {
  try {
    const response: IRunMentorRequestResponse = yield call(
      post,
      "/mentor-requests",
      action.requestBody
    );

    yield put(createMentorRequestSuccess(response.mentorRequestId));
  } catch (error) {
    yield put(createMentorRequestFailure(error.message));
  }
}

export function* fetchMentorRequestResultOverview(
  action: FetchMentorRequestResultOverview
) {
  try {
    const response: IFetchMentorRequestResultsOverviewResponse = yield call(
      get,
      `/mentor-requests/result/${action.requestBody.mentorRequestId}`
    );

    yield put(
      fetchMentorRequestResultOverviewSuccess(response.mentorRequestResult)
    );
  } catch (error) {
    yield put(fetchMentorRequestResultOverviewFailure(error.message));
  }
}

export function* fetchMentorList() {
  try {
    const response: IUser[] = yield call(get, `/mentors`);

    yield put(fetchMentorListSuccess(response));
  } catch (error) {
    yield put(fetchMentorListFailure(error.message));
  }
}

const mentorRequestsSagas = [
  takeLatest(CREATE_MENTOR_REQUEST_BEGIN, createMentorRequest),
  takeLatest(FETCH_MENTOR_REQUESTS_BEGIN, fetchMentorRequests),
  takeLatest(
    FETCH_MENTOR_REQUESTS_RESULT_OVERVIEW_BEGIN,
    fetchMentorRequestResultOverview
  ),
  takeLatest(FETCH_MENTOR_LIST_BEGIN, fetchMentorList)
];
export default mentorRequestsSagas;
