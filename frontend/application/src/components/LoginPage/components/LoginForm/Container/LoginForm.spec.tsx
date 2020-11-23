import React from "react";
import { shallow, ShallowWrapper } from "enzyme";
import { ILoginFormProps } from "./ILoginFormProps";
import LoginForm from "./LoginForm";
import LoginFormView from "../View/LoginFormView";

describe("Given a LoginForm component", () => {
  let wrapper: ShallowWrapper<ILoginFormProps>;
  const loginMock = jest.fn();

  beforeEach(() => {
    wrapper = shallow(
      <LoginForm
        login={loginMock}
        displayError={false}
        isAuthenticating={false}
      />
    );
  });

  describe("when rendering", () => {
    it("should have a LoginFormView component", () => {
      expect(wrapper.find(LoginFormView).exists()).toEqual(true);
    });
  });
});
