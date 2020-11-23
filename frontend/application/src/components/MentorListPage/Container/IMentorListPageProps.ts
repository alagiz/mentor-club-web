import { mapStateToProps, mapDispatchToProps } from "./MentorListPage";

export type IMentorListPageProps = ReturnType<typeof mapStateToProps> &
  typeof mapDispatchToProps;
