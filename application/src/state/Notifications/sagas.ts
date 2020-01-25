import {
  takeLatest,
  takeEvery,
  take,
  put,
  fork,
  delay,
  select
} from "redux-saga/effects";
import { APP_STARTED } from "../App/actions";
import {
  UPDATE_TASK_STATUS,
  UpdateMentorRequestStatus
} from "../MentorRequests/actions";
import {
  createMentorRequestUpdateNotification,
  deleteNotification,
  showNotification,
  DELETE_NOTIFICATION
} from "./actions";
import {
  selectNumberOfVisibleNotifications,
  selectOldestHiddenNotificationId
} from "./selectors";

const maxNumberOfVisibleNotifications = 4;

export function* createNotification(
  action: UpdateMentorRequestStatus,
  notificationId: number
) {
  const numberOfVisibleNotifications: number = yield select(
    selectNumberOfVisibleNotifications
  );

  const displayNotification: boolean =
    numberOfVisibleNotifications < maxNumberOfVisibleNotifications;

  yield put(
    createMentorRequestUpdateNotification(
      notificationId,
      action.mentorRequestId,
      action.newStatus,
      displayNotification
    )
  );

  if (displayNotification) {
    yield delay(5000);
    yield put(deleteNotification(notificationId));
  }
}

export function* listenToStatusNotifications() {
  let notificationIterator = 0;

  while (true) {
    const action: UpdateMentorRequestStatus = yield take(UPDATE_TASK_STATUS);
    yield fork(createNotification, action, notificationIterator++);
  }
}

export function* displayNextNotification() {
  const notificationId: number = yield select(selectOldestHiddenNotificationId);

  const numberOfVisibleNotifications: number = yield select(
    selectNumberOfVisibleNotifications
  );

  if (
    notificationId &&
    numberOfVisibleNotifications < maxNumberOfVisibleNotifications
  ) {
    yield put(showNotification(notificationId));
    yield delay(5000);
    yield put(deleteNotification(notificationId));
  }
}

const notificationSagas = [
  takeLatest([APP_STARTED], listenToStatusNotifications),
  takeEvery([DELETE_NOTIFICATION], displayNextNotification)
];

export default notificationSagas;
