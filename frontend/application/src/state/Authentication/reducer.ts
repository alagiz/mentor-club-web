import { IAuthenticationState } from "./types";
import {
  AUTHENTICATE_BEGIN,
  AUTHENTICATE_FAILURE,
  AUTHENTICATE_SUCCESS,
  LOGOUT,
  AuthenticationActionTypes
} from "./actions";
import { getToken } from "../../service/UserManager/UserManager";

const initialState: IAuthenticationState = {
  isLoading: false,
  isAuthenticated: !!getToken(),
  displayError: false
};

export const authenticationReducer = (
  state: IAuthenticationState = initialState,
  action: AuthenticationActionTypes
): IAuthenticationState => {
  switch (action.type) {
    case AUTHENTICATE_BEGIN: {
      return {
        ...state,
        isLoading: true,
        displayError: false
      };
    }
    case AUTHENTICATE_SUCCESS:
      return {
        ...state,
        isLoading: false,
        isAuthenticated: true
      };
    case AUTHENTICATE_FAILURE: {
      return {
        ...state,
        isLoading: false,
        displayError: true
      };
    }
    case LOGOUT: {
      return {
        ...state,
        isAuthenticated: false
      };
    }
    default: {
      return state;
    }
  }
};
