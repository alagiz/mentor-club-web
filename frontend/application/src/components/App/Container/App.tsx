import React from "react";
import { connect } from "react-redux";

import AppView from "../View/AppView";
import { appStarted } from "../../../state/App/actions";
import { IAppProps } from "./IAppProps";
import { logout } from "../../../state/Authentication/actions";

export const App: React.FC<IAppProps> = ({ appStarted, logout }) => {
  React.useEffect(() => {
    appStarted();
  }, [appStarted]);

  return <AppView logout={logout} />;
};

export const mapDispatchToProps = {
  logout,
  appStarted
} as const;

export default connect(null, mapDispatchToProps)(App);
