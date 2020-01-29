import {
  mapStateToProps,
  mapDispatchToProps
} from "./MentorRequestCreationPage";

export type IMentorRequestCreationPageProps = ReturnType<
  typeof mapStateToProps
> &
  typeof mapDispatchToProps;
