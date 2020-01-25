/* global require module */
// eslint-disable-next-line @typescript-eslint/no-var-requires
const userTestData = require("../../integration-tests/src/fixtures/userTestData");
const mentorRequestList = require("../../integration-tests/src/fixtures/mentorRequestList");
const mentorRequestResult = require("../../integration-tests/src/fixtures/mentorRequestResult");
const createMentorRequest = require("../../integration-tests/src/fixtures/createMentorRequestResponse");

module.exports = () => {
  return {
    mentorRequestList,
    mentorRequestResult,
    userTestData,
    createMentorRequest,
    validateToken: {}
  };
};
