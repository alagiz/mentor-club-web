import { Selector } from "../../types/Selector";

export const selectIsAuthenticated: Selector<boolean> = state =>
  state.authentication.isAuthenticated;

export const selectIsLoading: Selector<boolean> = state =>
  state.authentication.isLoading;

export const selectDisplayError: Selector<boolean> = state =>
  state.authentication.displayError;
