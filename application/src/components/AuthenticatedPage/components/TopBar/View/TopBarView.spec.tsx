import { shallow } from "enzyme";
import React from "react";
import TopBarView from "./TopBarView";

describe("Given a TopBarView", () => {
  describe("when rendering", () => {
    it("should have TopBarView matching snapshot", () => {
      const topBarView = shallow(<TopBarView logout={jest.fn()} />);

      expect(topBarView).toMatchSnapshot();
    });
  });
});
