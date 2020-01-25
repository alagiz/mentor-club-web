import { mapStateToProps, mapDispatchToProps } from "./MentorRequestListPage";

export type IMentorRequestListPageProps = ReturnType<typeof mapStateToProps> &
  typeof mapDispatchToProps;
