import React from "react";
import { shallow } from "enzyme";
import { MentorRequestCreationPage } from "./MentorRequestCreationPage";

jest.mock("react-router-dom");
jest.mock("react-router", () => ({
  useHistory: () => ({
    push: jest.fn()
  }),
  useLocation: () => ({
    state: {
      detail: ""
    }
  })
}));

describe("Given a MentorRequestCreationPage", () => {
  it("should have MentorRequestCreationPage matching snapshot", () => {
    const component = shallow(
      <MentorRequestCreationPage
        mentors={[]}
        isFetchingMentorList={false}
        clearNewMentorRequestId={jest.fn()}
        createMentorRequestBegin={jest.fn()}
        fetchMentorListBegin={jest.fn()}
        newMentorRequestId={""}
      />
    );

    expect(component).toMatchSnapshot();
  });
});
