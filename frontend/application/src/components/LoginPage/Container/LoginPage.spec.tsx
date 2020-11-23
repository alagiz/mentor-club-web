import React from "react";
import { shallow } from "enzyme";
import { ILoginPageProps } from "./ILoginPageProps";
import { LoginPage } from "./LoginPage";
import LoginPageView from "../View/LoginPageView";
import { Redirect } from "react-router-dom";

describe("Given a LoginPage component", () => {
  const props: ILoginPageProps = {
    isLoading: false,
    isAuthenticated: false,
    authenticateBegin: jest.fn(),
    displayError: false
  };

  describe("when user is not authenticated", () => {
    it("should have a LoginPageView component", () => {
      const component = shallow(<LoginPage {...props} />);

      expect(component.find(LoginPageView).exists()).toEqual(true);
    });
  });

  describe("when user is authenticated", () => {
    it("should have a Redirect component", () => {
      const modifiedProps: ILoginPageProps = {
        ...props,
        isAuthenticated: true
      };

      const component = shallow(<LoginPage {...modifiedProps} />);

      expect(component.find(Redirect).exists()).toEqual(true);
    });
  });
});
