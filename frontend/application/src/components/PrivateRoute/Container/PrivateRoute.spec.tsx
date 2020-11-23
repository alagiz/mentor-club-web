import React from "react";
import { IPrivateRouteProps } from "./IPrivateRouteProps";
import { shallow, ShallowWrapper } from "enzyme";
import { PrivateRoute } from "./PrivateRoute";
import { Redirect } from "react-router-dom";

describe("Given a PrivateRoute component", () => {
  let wrapper: ShallowWrapper<IPrivateRouteProps>;
  const props: IPrivateRouteProps = {
    children: <div />,
    isAuthenticated: false
  };

  describe("when user is not authenticated", () => {
    it("should render the redirect component", () => {
      wrapper = shallow(<PrivateRoute {...props} />);

      expect(wrapper.find(Redirect).exists()).toBe(true);
    });
  });

  describe("when user is authenticated", () => {
    it("should render its children", () => {
      const modifiedProps = { ...props, isAuthenticated: true };
      wrapper = shallow(<PrivateRoute {...modifiedProps} />);

      expect(wrapper.find("div").exists()).toBe(true);
    });
  });
});
