import { IAppState } from "../../../store";
import {
  selectDisplayError,
  selectIsAuthenticated,
  selectIsLoading
} from "../selectors";

const state = {
  authentication: {
    isAuthenticated: false,
    isLoading: true,
    displayError: true
  }
} as IAppState;

describe("selectIsAuthenticated", () => {
  it("should return if the user is authenticated", () => {
    expect(selectIsAuthenticated(state)).toEqual(false);
  });
});

describe("selectIsLoading", () => {
  it("should return if the user's authentication request is loading", () => {
    expect(selectIsLoading(state)).toEqual(true);
  });
});

describe("selectDisplayError", () => {
  it("should return if an error needs to be displayed", () => {
    expect(selectDisplayError(state)).toEqual(true);
  });
});
