import React from "react";
import { shallow } from "enzyme";
import { ILoginPageViewProps } from "./ILoginPageViewProps";
import LoginPageView from "./LoginPageView";

describe("Given LoginPageView", () => {
  describe("when rendering", () => {
    it("should have LoginFormView matching snapshot", () => {
      const props: ILoginPageViewProps = {
        login: jest.fn(),
        isAuthenticating: false,
        displayError: false
      };

      const component = shallow(<LoginPageView {...props} />);

      expect(component).toMatchSnapshot();
    });
  });
});
