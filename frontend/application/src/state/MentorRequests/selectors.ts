import { Selector } from "types/Selector";
import { IMentorRequest, IUser } from "./types";
import { IAppState } from "../../store";

export const selectMentorRequests: Selector<IMentorRequest[] | null> = state =>
  state.mentorRequests.mentorRequests;

export const selectIsFetchingMentorRequests: Selector<boolean> = state =>
  state.mentorRequests.isFetchingMentorRequests;

export const selectIsFetchingMentors: Selector<boolean> = state =>
  state.mentorRequests.isFetchingMentorList;

export const selectMentors: Selector<IUser[] | null> = (state: IAppState) =>
  state.mentorRequests.mentors;

export const selectFetchMentorRequestsError: Selector<string | null> = state =>
  state.mentorRequests.fetchMentorRequestsError;

export const selectIsCreatingMentorRequest: Selector<boolean> = state =>
  state.mentorRequests.isCreatingMentorRequest;

export const selectNewMentorRequestId: Selector<string | null> = state =>
  state.mentorRequests.newMentorRequestId;
