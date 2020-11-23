import React from "react";
import { shallow } from "enzyme";
import LoginFormView from "./LoginFormView";
import { ILoginFormViewProps } from "./ILoginFormViewProps";

describe("Given LoginFormView", () => {
  describe("when rendering", () => {
    it("should have LoginFormView matching snapshot", () => {
      const props: ILoginFormViewProps = {
        onSubmitForm: jest.fn(),
        onChangePassword: jest.fn(),
        onChangeEmail: jest.fn(),
        email: "test@test.com",
        password: "fake",
        isAuthenticating: false,
        displayError: false
      };

      const component = shallow(<LoginFormView {...props} />);

      expect(component).toMatchSnapshot();
    });
  });
});
