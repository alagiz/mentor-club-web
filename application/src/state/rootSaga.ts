import { all } from "redux-saga/effects";
import authenticationSagas from "./Authentication/sagas";
import mentorRequestsSagas from "./MentorRequests/sagas";
import notificationSagas from "./Notifications/sagas";

export function* rootSaga() {
  yield all([
    ...authenticationSagas,
    ...mentorRequestsSagas,
    ...notificationSagas
  ]);
}
