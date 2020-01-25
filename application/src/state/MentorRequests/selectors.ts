import { groupBy } from "ramda";
import { Selector } from "types/Selector";
import {
  IResultWafer,
  IMentorRequest,
  IMentorRequestResult,
  IWaferDetails
} from "./types";

export const selectMentorRequests: Selector<IMentorRequest[] | null> = state =>
  state.mentorRequests.mentorRequests;

export const selectMentorRequestResult: Selector<IMentorRequestResult | null> = state =>
  state.mentorRequests.mentorRequestResult;

export const selectMentorRequestResultWafers: Selector<{
  [waferId: string]: IWaferDetails;
}> = state => state.mentorRequests.mentorRequestResultWafers;

export const selectIsFetchingMentorRequestResult: Selector<boolean> = state =>
  state.mentorRequests.isFetchingMentorRequestResultOverview;

export const selectFetchMentorRequestResultError: Selector<
  string | null
> = state => state.mentorRequests.fetchMentorRequestResultOverviewError;

export const selectFetchMentorRequestResultWaferError: Selector<
  string | null
> = state => state.mentorRequests.fetchMentorRequestResultWaferError;

export const selectIsFetchingMentorRequests: Selector<boolean> = state =>
  state.mentorRequests.isFetchingMentorRequests;

export const selectIsFetchingMentorRequestResultWafer: Selector<boolean> = state =>
  state.mentorRequests.isFetchingMentorRequestResultWafer;

export const selectFetchMentorRequestsError: Selector<string | null> = state =>
  state.mentorRequests.fetchMentorRequestsError;

export const selectMentorRequestResultPerChamberId: Selector<{
  [chamberId: string]: IResultWafer[];
} | null> = state => {
  if (!state.mentorRequests.mentorRequestResult) return null;

  const byChamberId = groupBy((wafer: IResultWafer) => wafer.chamberId);

  return byChamberId(state.mentorRequests.mentorRequestResult.wafers);
};

export const selectIsCreatingMentorRequest: Selector<boolean> = state =>
  state.mentorRequests.isCreatingMentorRequest;

export const selectNewMentorRequestId: Selector<string | null> = state =>
  state.mentorRequests.newMentorRequestId;
