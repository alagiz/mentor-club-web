import React from "react";
import { Redirect } from "react-router-dom";
import { connect } from "react-redux";
import { IAppState } from "../../../store";
import { IPrivateRouteProps } from "./IPrivateRouteProps";
import { selectIsAuthenticated } from "../../../state/Authentication/selectors";

export const PrivateRoute: React.FC<IPrivateRouteProps> = ({
  children,
  isAuthenticated
}) => {
  return isAuthenticated ? <>{children}</> : <Redirect to="/login" />;
};

export const mapStateToProps = (state: IAppState) => ({
  isAuthenticated: selectIsAuthenticated(state)
});

export default connect(mapStateToProps)(PrivateRoute);
