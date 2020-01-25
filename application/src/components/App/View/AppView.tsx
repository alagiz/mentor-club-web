import React from "react";
import { BrowserRouter as Router, Route, Redirect } from "react-router-dom";
import LoginPage from "../../LoginPage/Container/LoginPage";
import PrivateRoute from "../../PrivateRoute/Container/PrivateRoute";
import MentorRequestListPage from "../../MentorRequestListPage/Container/MentorRequestListPage";
import AuthenticatedPage from "../../AuthenticatedPage/Container/AuthenticatedPage";
import { IAppViewProps } from "./IAppViewProps";

const AppView: React.FC<IAppViewProps> = ({ logout }) => (
  <Router basename="/mentor-club">
    <PrivateRoute>
      <AuthenticatedPage logout={logout}>
        <Route exact path="/">
          <Redirect to="/mentor-requests" />
        </Route>
        <Route exact path="/mentor-requests">
          <MentorRequestListPage />
        </Route>
        <Route exact path="/mentor-requests/new"></Route>
      </AuthenticatedPage>
    </PrivateRoute>
    <Route exact path="/login">
      <LoginPage />
    </Route>
  </Router>
);

export default AppView;
