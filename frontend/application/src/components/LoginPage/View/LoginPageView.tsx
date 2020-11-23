import React from "react";
import LoginForm from "../components/LoginForm/Container/LoginForm";
import { Styled } from "../Styled";
import { ILoginPageViewProps } from "./ILoginPageViewProps";
import translator from "../../../config/i18next";

const LoginPageView: React.FC<ILoginPageViewProps> = ({
  login,
  isAuthenticating,
  displayError
}) => {
  return (
    <Styled.LoginWrapper>
      <Styled.LoginFormContainer>
        <div>
          <header>
            <Styled.LoginHeaderText>
              {translator.t("general.applicationTitle")}
              <Styled.VersionNumber>
                {translator.t("general.versionNumber")}
              </Styled.VersionNumber>
            </Styled.LoginHeaderText>
            <LoginForm
              login={login}
              displayError={displayError}
              isAuthenticating={isAuthenticating}
            />
          </header>
        </div>
      </Styled.LoginFormContainer>
    </Styled.LoginWrapper>
  );
};

export default LoginPageView;
