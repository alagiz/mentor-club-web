import React from "react";
import { shallow } from "enzyme";
import MentorListPageView from "./MentorListPageView";

describe("Given MentorListPageView", () => {
  describe("when rendering", () => {
    it("should have MentorListPageView matching snapshot", () => {
      const component = shallow(
        <MentorListPageView
          isFetchingMentorList={false}
          mentors={[]}
          handleChooseMentorClick={jest.fn()}
        />
      );

      expect(component).toMatchSnapshot();
    });
  });
});
