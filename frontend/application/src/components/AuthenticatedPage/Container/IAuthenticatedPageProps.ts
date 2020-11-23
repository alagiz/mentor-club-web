import { IAppProps } from "../../App/Container/IAppProps";
import { mapDispatchToProps, mapStateToProps } from "./AuthenticatedPage";

export type IAuthenticatedPageProps = {
  logout: IAppProps["logout"];
  children: React.ReactElement[];
} & ReturnType<typeof mapStateToProps> &
  typeof mapDispatchToProps;
