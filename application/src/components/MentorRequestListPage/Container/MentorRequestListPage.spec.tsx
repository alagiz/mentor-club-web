import React from "react";
import { shallow } from "enzyme";
import { MentorRequestListPage } from "./MentorRequestListPage";

jest.mock("react-router-dom", () => ({
  useHistory: () => ({
    push: jest.fn()
  })
}));

describe("Given MentorRequestCreationPage", () => {
  it("should have MentorRequestResultPage matching snapshot", () => {
    const component = shallow(
      <MentorRequestListPage
        mentorRequests={[]}
        error={null}
        isLoading={false}
        fetchMentorRequestsBegin={jest.fn()}
      />
    );

    expect(component).toMatchSnapshot();
  });
});
