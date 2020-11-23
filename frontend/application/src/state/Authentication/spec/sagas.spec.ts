import MockAdapter from "axios-mock-adapter";
import axios from "axios";
import { runSaga } from "redux-saga";
import { AnyAction } from "redux";
import * as apiService from "../../../service/Api/Api";
import * as sagas from "../sagas";
import {
  removeUserFromLocalStorage,
  storeToken,
  storeThumbnail,
  storeUsername,
  storeDisplayName
} from "../../../service/UserManager/UserManager";
import {
  authenticateBegin,
  authenticateFailure,
  authenticateSuccess
} from "../actions";

const mockApi = new MockAdapter(axios);

jest.mock("../../../store.ts");
jest.mock("../../../service/UserManager/UserManager", () => {
  return {
    removeUserFromLocalStorage: jest.fn(() => true),
    storeToken: jest.fn(),
    storeThumbnail: jest.fn(),
    storeUsername: jest.fn(),
    storeDisplayName: jest.fn(),
    getToken: () => true
  };
});

beforeEach(() => {
  jest.resetAllMocks();
  mockApi.reset();
});

describe("Authentication sagas", () => {
  describe("logoutUser saga", () => {
    it("should call removeUserFromLocalStorage", async () => {
      await runSaga({}, sagas.logoutUser).toPromise();
      expect(removeUserFromLocalStorage).toBeCalled();
    });
  });

  describe("authenticateUser saga", () => {
    it("should call the api service", async () => {
      const spy = spyOn(apiService, "post");

      await runSaga(
        { dispatch: () => undefined },
        sagas.authenticateUser,
        authenticateBegin("test@test.nl", "fakePassword")
      ).toPromise();

      expect(spy).toHaveBeenCalled();
    });

    it("should dispatch authenticateFailure on authentication failure", async () => {
      const dispatched: AnyAction[] = [];

      mockApi.onAny().reply(500);

      await runSaga(
        { dispatch: (action: AnyAction) => dispatched.push(action) },
        sagas.authenticateUser,
        authenticateBegin("test@test.nl", "fakePassword")
      ).toPromise();

      expect(dispatched).toStrictEqual([
        authenticateFailure("An unexpected error occurred")
      ]);
    });

    it("should dispatch authenticateSuccess and store user information on authentication success ", async () => {
      const dispatched: AnyAction[] = [];
      const fakeResponse = {
        username: "test@test.nl",
        token: "co0lToKeN",
        displayname: "Test Man",
        thumbnailphoto: "base64ImageString"
      };

      mockApi.onAny().reply(200, fakeResponse);

      await runSaga(
        { dispatch: (action: AnyAction) => dispatched.push(action) },
        sagas.authenticateUser,
        authenticateBegin("test@test.nl", "fakePassword")
      ).toPromise();

      expect(dispatched).toStrictEqual([authenticateSuccess()]);
      expect(storeToken).toBeCalledWith(fakeResponse.token);
      expect(storeThumbnail).toBeCalledWith(fakeResponse.thumbnailphoto);
      expect(storeUsername).toBeCalledWith(fakeResponse.username);
      expect(storeDisplayName).toBeCalledWith(fakeResponse.displayname);
    });
  });

  describe("validateToken saga", () => {
    it("should do nothing when the user is not authenticated", async () => {
      const spy = spyOn(apiService, "get");
      await runSaga(
        {
          getState: () => ({
            authentication: {
              isAuthenticated: false
            }
          })
        },
        sagas.validateToken
      ).toPromise();

      expect(spy).not.toHaveBeenCalled();
    });

    it("should validate the token when the user is authenticated", async () => {
      const spy = spyOn(apiService, "post");
      await runSaga(
        {
          getState: () => ({
            authentication: {
              isAuthenticated: true
            }
          })
        },
        sagas.validateToken
      ).toPromise();

      expect(spy).toHaveBeenCalled();
    });
  });
});
