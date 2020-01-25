import React from "react";
import { shallow } from "enzyme";
import MentorRequestListPageView from "./MentorRequestListPageView";

describe("Given MentorRequestResultPageView", () => {
  describe("when rendering", () => {
    it("should have MentorRequestResultPageView matching snapshot", () => {
      const component = shallow(
        <MentorRequestListPageView
          isLoading={false}
          error={null}
          mentorRequests={[]}
          handleRowClick={jest.fn()}
        />
      );

      expect(component).toMatchSnapshot();
    });
  });
});
