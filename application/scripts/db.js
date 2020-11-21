/* global require module */
// eslint-disable-next-line @typescript-eslint/no-var-requires
const userTestData = require("./fixtures/userTestData");
const mentorRequestList = require("./fixtures/mentorRequestList");
const mentorList = require("./fixtures/mentorList");
const mentorRequestResult = require("./fixtures/mentorRequestResult");
const createMentorRequest = require("./fixtures/createMentorRequestResponse");

module.exports = () => {
  return {
    mentorRequestList,
    mentorRequestResult,
    userTestData,
    createMentorRequest,
    mentorList,
    validateToken: {}
  };
};
