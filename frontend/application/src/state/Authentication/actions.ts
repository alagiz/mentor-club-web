export const AUTHENTICATE_BEGIN = "authentication/AUTHENTICATE_BEGIN";
export const AUTHENTICATE_SUCCESS = "authentication/AUTHENTICATE_SUCCESS";
export const AUTHENTICATE_FAILURE = "authentication/AUTHENTICATE_FAILURE";
export const LOGOUT = "authentication/LOGOUT";

export const authenticateBegin = (email: string, password: string) => {
  return {
    type: AUTHENTICATE_BEGIN,
    email,
    password
  } as const;
};

export const authenticateSuccess = () => {
  return {
    type: AUTHENTICATE_SUCCESS
  } as const;
};

export const authenticateFailure = (displayError: string) => {
  return {
    type: AUTHENTICATE_FAILURE,
    error: displayError
  } as const;
};

export const logout = () => {
  return {
    type: LOGOUT
  } as const;
};

export type AuthenticateBeginType = ReturnType<typeof authenticateBegin>;

export type AuthenticationActionTypes = ReturnType<
  | typeof authenticateBegin
  | typeof authenticateSuccess
  | typeof authenticateFailure
  | typeof logout
>;
