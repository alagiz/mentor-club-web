import React, { useState } from "react";

import LoginFormView from "../View/LoginFormView";
import { ILoginFormProps } from "./ILoginFormProps";

const LoginForm: React.FC<ILoginFormProps> = ({
  login,
  isAuthenticating,
  displayError
}) => {
  const [email, setEmail] = useState<string>("");
  const [password, setPassword] = useState<string>("");

  const handleEmailChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setEmail(event.target.value);
  };
  const handlePasswordChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setPassword(event.target.value);
  };
  const handleSubmitForm = (event: React.FormEvent) => {
    login(email, password);
    event.preventDefault();
  };

  return (
    <LoginFormView
      onSubmitForm={handleSubmitForm}
      onChangeEmail={handleEmailChange}
      onChangePassword={handlePasswordChange}
      email={email}
      password={password}
      isAuthenticating={isAuthenticating}
      displayError={displayError}
    />
  );
};

export default LoginForm;
