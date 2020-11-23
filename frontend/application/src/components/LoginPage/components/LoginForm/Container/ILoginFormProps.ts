import { ILoginPageProps } from "../../../Container/ILoginPageProps";

export interface ILoginFormProps {
  login: (email: string, password: string) => void;
  isAuthenticating: ILoginPageProps["isAuthenticated"];
  displayError: ILoginPageProps["displayError"];
}
