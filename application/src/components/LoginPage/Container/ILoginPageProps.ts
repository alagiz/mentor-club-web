import { mapDispatchToProps, mapStateToProps } from "./LoginPage";

export type ILoginPageProps = ReturnType<typeof mapStateToProps> &
  typeof mapDispatchToProps;
