import { applyMiddleware, combineReducers, compose, createStore } from "redux";
import createSagaMiddleware from "redux-saga";
import { authenticationReducer } from "./state/Authentication/reducer";
import { notificationsReducer } from "./state/Notifications/reducer";
import { mentorRequestsReducer } from "./state/MentorRequests/reducer";
import { rootSaga } from "./state/rootSaga";
import { IMentorRequestsState } from "./state/MentorRequests/types";
import { IAuthenticationState } from "./state/Authentication/types";
import { INotificationsState } from "./state/Notifications/types";

export interface IAppState {
  authentication: IAuthenticationState;
  notifications: INotificationsState;
  mentorRequests: IMentorRequestsState;
}

const rootReducer = combineReducers({
  authentication: authenticationReducer,
  notifications: notificationsReducer,
  mentorRequests: mentorRequestsReducer
});

const sagaMiddleware = createSagaMiddleware();

let composeEnhancers: any;
if (process.env.NODE_ENV === "development") {
  // @ts-ignore
  composeEnhancers = window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ || compose;
} else {
  composeEnhancers = compose;
}

const store = createStore(
  rootReducer,
  composeEnhancers(applyMiddleware(sagaMiddleware))
);

sagaMiddleware.run(rootSaga);

export { store };
