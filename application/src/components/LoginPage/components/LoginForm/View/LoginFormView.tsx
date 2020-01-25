import React from "react";
import { Styled } from "../Styled";
import { ILoginFormViewProps } from "./ILoginFormViewProps";
import translator from "../../../../../config/i18next";

const LoginFormView: React.FC<ILoginFormViewProps> = ({
  onSubmitForm,
  onChangePassword,
  onChangeEmail,
  email,
  password,
  isAuthenticating,
  displayError
}): JSX.Element => {
  return (
    <Styled.LoginForm onSubmit={onSubmitForm}>
      <Styled.LoginFormInput
        type="text"
        value={email}
        onChange={onChangeEmail}
        data-id="login-email-input"
        placeholder={translator.t("loginPage.enterEmail")}
      />
      <Styled.LoginFormInput
        type="password"
        value={password}
        onChange={onChangePassword}
        data-id="login-password-input"
        placeholder={translator.t("loginPage.enterPassword")}
      />

      {displayError && (
        <Styled.LoginFormErrorMessage data-id="login-error-message">
          {translator.t("loginPage.invalidCredentials")}
        </Styled.LoginFormErrorMessage>
      )}

      <Styled.LoginFormButton
        loading={isAuthenticating}
        type="primary"
        htmlType="submit"
        size="large"
        data-id="login-submit-button"
      >
        {translator.t("loginPage.login")}
      </Styled.LoginFormButton>
    </Styled.LoginForm>
  );
};

export default LoginFormView;
