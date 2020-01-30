import React from "react";
import { shallow } from "enzyme";
import { MentorListPage } from "./MentorListPage";

jest.mock("react-router-dom");
jest.mock("react-router", () => ({
  useHistory: () => ({
    push: jest.fn()
  })
}));

describe("Given MentorListPage", () => {
  it("should have MentorListPage matching snapshot", () => {
    const component = shallow(
      <MentorListPage
        mentors={[]}
        fetchMentorListBegin={jest.fn()}
        isFetchingMentorList={false}
      />
    );

    expect(component).toMatchSnapshot();
  });
});
