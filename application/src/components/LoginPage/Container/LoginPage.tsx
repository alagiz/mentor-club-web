import React from "react";
import { connect } from "react-redux";

import LoginPageView from "../View/LoginPageView";
import { authenticateBegin } from "../../../state/Authentication/actions";
import { IAppState } from "../../../store";
import { ILoginPageProps } from "./ILoginPageProps";
import { Redirect } from "react-router-dom";
import {
  selectDisplayError,
  selectIsAuthenticated,
  selectIsLoading
} from "../../../state/Authentication/selectors";

export const LoginPage: React.FC<ILoginPageProps> = ({
  isLoading,
  isAuthenticated,
  authenticateBegin,
  displayError
}) => {
  const handleLoginRequest = (email: string, password: string) => {
    authenticateBegin(email, password);
  };

  if (isAuthenticated) {
    return <Redirect to="/" />;
  }

  return (
    <LoginPageView
      login={handleLoginRequest}
      displayError={displayError}
      isAuthenticating={isLoading}
    />
  );
};

export const mapStateToProps = (state: IAppState) => ({
  isLoading: selectIsLoading(state),
  isAuthenticated: selectIsAuthenticated(state),
  displayError: selectDisplayError(state)
});

export const mapDispatchToProps = {
  authenticateBegin
} as const;

export default connect(mapStateToProps, mapDispatchToProps)(LoginPage);
