export interface ILoginPageViewProps {
  login: (email: string, password: string) => void;
  isAuthenticating: boolean;
  displayError: boolean;
}
