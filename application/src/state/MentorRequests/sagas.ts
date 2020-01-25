import { call, put, takeLatest } from "@redux-saga/core/effects";
import { get, post } from "service/Api/Api";
import {
  CREATE_TASK_BEGIN,
  CreateMentorRequestBegin,
  createMentorRequestFailure,
  createMentorRequestSuccess,
  FETCH_TASKS_BEGIN,
  FETCH_TASKS_RESULT_OVERVIEW_BEGIN,
  FETCH_TASKS_RESULT_WAFER_BEGIN,
  FetchMentorRequestResultOverview,
  fetchMentorRequestResultOverviewFailure,
  fetchMentorRequestResultOverviewSuccess,
  FetchMentorRequestResultWafer,
  fetchMentorRequestResultWaferFailure,
  fetchMentorRequestResultWaferSuccess,
  fetchMentorRequestsFailure,
  fetchMentorRequestsSuccess
} from "./actions";
import {
  IFetchMentorRequestResultsDetailResponse,
  IFetchMentorRequestResultsOverviewResponse,
  IRunMentorRequestResponse,
  IMentorRequest
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

export function* fetchMentorRequestResultWafer(
  action: FetchMentorRequestResultWafer
) {
  try {
    const response: IFetchMentorRequestResultsDetailResponse = yield call(
      get,
      `/mentor-requests/${action.requestBody.mentorRequestId}`,
      {
        wmsId: action.requestBody.wmsId
      }
    );

    yield put(
      fetchMentorRequestResultWaferSuccess(
        action.requestBody.wmsId,
        response.wafer
      )
    );
  } catch (error) {
    yield put(fetchMentorRequestResultWaferFailure(error.message));
  }
}

const mentorRequestsSagas = [
  takeLatest(CREATE_TASK_BEGIN, createMentorRequest),
  takeLatest(FETCH_TASKS_BEGIN, fetchMentorRequests),
  takeLatest(
    FETCH_TASKS_RESULT_OVERVIEW_BEGIN,
    fetchMentorRequestResultOverview
  ),
  takeLatest(FETCH_TASKS_RESULT_WAFER_BEGIN, fetchMentorRequestResultWafer)
];
export default mentorRequestsSagas;
