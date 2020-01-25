import { IMentorRequestStatus } from "../../state/MentorRequests/types";
import { scopedTranslate } from "../../config/i18next";

const translate = scopedTranslate("mentorRequests.statuses");

export const translateMentorRequestStatus = (
  status: IMentorRequestStatus
): string => {
  switch (status) {
    case "CREATED":
      return translate("created");
    case "IN_PROGRESS":
      return translate("inProgress");
    case "FAILED":
      return translate("failed");
    case "DONE":
      return translate("done");
    default:
      return translate("unknown");
  }
};
