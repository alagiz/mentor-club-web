import { IAuthenticationState } from "../types";
import { authenticationReducer } from "../reducer";
import {
  authenticateBegin,
  authenticateFailure,
  authenticateSuccess,
  logout
} from "../actions";

describe("Authentication reducer", () => {
  const initialState: IAuthenticationState = {
    isLoading: false,
    isAuthenticated: false,
    displayError: false
  };

  it("should handle authenticate begin", () => {
    const result: IAuthenticationState = authenticationReducer(
      initialState,
      authenticateBegin("test@test.nl", "fakePassword")
    );

    expect(result).toStrictEqual({
      ...initialState,
      isLoading: true
    });
  });

  it("should handle authenticate success", () => {
    const result: IAuthenticationState = authenticationReducer(
      initialState,
      authenticateSuccess()
    );

    expect(result).toStrictEqual({
      ...initialState,
      isAuthenticated: true
    });
  });

  it("should handle authenticate failure", () => {
    const result: IAuthenticationState = authenticationReducer(
      initialState,
      authenticateFailure("failure")
    );

    expect(result).toStrictEqual({
      ...initialState,
      displayError: true
    });
  });

  it("should handle logout", () => {
    const result: IAuthenticationState = authenticationReducer(
      initialState,
      logout()
    );

    expect(result).toStrictEqual({
      ...initialState,
      isAuthenticated: false
    });
  });
});
