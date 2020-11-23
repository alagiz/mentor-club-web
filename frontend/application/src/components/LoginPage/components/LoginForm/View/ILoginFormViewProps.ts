import React from "react";

export interface ILoginFormViewProps {
  email: string;
  password: string;
  onSubmitForm: React.FormEventHandler;
  onChangePassword: React.ChangeEventHandler;
  onChangeEmail: React.ChangeEventHandler;
  isAuthenticating: boolean;
  displayError: boolean;
}
