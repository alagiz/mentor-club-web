import { call, put, takeLatest, delay, select } from "redux-saga/effects";
import {
  AUTHENTICATE_BEGIN,
  LOGOUT,
  AuthenticateBeginType,
  authenticateFailure,
  authenticateSuccess
} from "./actions";
import { post } from "../../service/Api/Api";
import {
  removeUserFromLocalStorage,
  storeDisplayName,
  storeThumbnail,
  storeToken,
  storeUsername
} from "../../service/UserManager/UserManager";
import { APP_STARTED } from "../App/actions";
import { selectIsAuthenticated } from "./selectors";

export function* authenticateUser(action: AuthenticateBeginType) {
  try {
    const response = yield call(post, "/authenticate", {
      username: action.email,
      password: action.password
    });

    yield call(storeToken, response.token);
    yield call(storeThumbnail, response.thumbnailphoto);
    yield call(storeUsername, response.username);
    yield call(storeDisplayName, response.displayname);

    yield put(authenticateSuccess());
  } catch (error) {
    yield put(authenticateFailure(error.message));
  }
}

export function* validateToken() {
  const isAuthenticated = yield select(selectIsAuthenticated);

  if (!isAuthenticated) {
    return;
  }

  try {
    yield call(post, "/validate-jwt");
  } catch (error) {}
}

function* validateTokenPeriodically() {
  while (true) {
    yield call(validateToken);
    yield delay(300000);
  }
}

export function* logoutUser() {
  yield call(removeUserFromLocalStorage);
}

const authenticationSagas = [
  takeLatest(APP_STARTED, validateTokenPeriodically),
  takeLatest(AUTHENTICATE_BEGIN, authenticateUser),
  takeLatest(LOGOUT, logoutUser)
];

export default authenticationSagas;
