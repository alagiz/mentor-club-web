import React from "react";
import { shallow } from "enzyme";
import TopBar from "./TopBar";

describe("Given a TopBar", () => {
  describe("when rendering", () => {
    it("should have TopBar matching snapshot", () => {
      const topBar = shallow(<TopBar logout={jest.fn()} />);

      expect(topBar).toMatchSnapshot();
    });
  });
});
