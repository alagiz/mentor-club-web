import { translateMentorRequestStatus } from "./MentorRequestStatusHelper";
import translator from "../../config/i18next";
import { IMentorRequestStatus } from "../../state/MentorRequests/types";

describe("translateMentorRequestStatus", () => {
  it("should retrieve the correct translation for the given mentor request status", () => {
    expect(translateMentorRequestStatus("CREATED")).toBe(
      translator.t("mentorRequests.statuses.created")
    );
    expect(translateMentorRequestStatus("IN_PROGRESS")).toBe(
      translator.t("mentorRequests.statuses.inProgress")
    );
    expect(translateMentorRequestStatus("FAILED")).toBe(
      translator.t("mentorRequests.statuses.failed")
    );
    expect(translateMentorRequestStatus("DONE")).toBe(
      translator.t("mentorRequests.statuses.done")
    );
    expect(
      translateMentorRequestStatus("FAKE_STATUS" as IMentorRequestStatus)
    ).toBe(translator.t("mentorRequests.statuses.unknown"));
  });
});
