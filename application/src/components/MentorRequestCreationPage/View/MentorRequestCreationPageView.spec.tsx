import React from "react";
import { shallow } from "enzyme";
import MentorRequestCreationPageView from "./MentorRequestCreationPageView";

describe("Given MentorRequestResultPageView", () => {
  describe("when rendering", () => {
    it("should have MentorRequestResultPageView matching snapshot", () => {
      const component = shallow(
        <MentorRequestCreationPageView
          isLoading={false}
          selectedMentor={null}
          createMentorRequest={jest.fn()}
          mentorRequestDescription={""}
          mentors={[]}
          setSelectedMentor={jest.fn()}
          updateMentorRequestDescription={jest.fn()}
        />
      );

      expect(component).toMatchSnapshot();
    });
  });
});
