import React from "react";
import { shallow } from "enzyme";
import MentorRequestCreationPageView from "./MentorRequestCreationPageView";

describe("Given MentorRequestCreationPageView", () => {
  describe("when rendering", () => {
    it("should have MentorRequestCreationPageView matching snapshot", () => {
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
