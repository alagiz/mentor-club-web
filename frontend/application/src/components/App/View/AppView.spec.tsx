import { shallow } from "enzyme";
import React from "react";
import AppView from "./AppView";

describe("Given AppView", () => {
  describe("when rendering", () => {
    it("should have AppView matching snapshot", () => {
      const appView = shallow(<AppView logout={jest.fn()} />);

      expect(appView).toMatchSnapshot();
    });
  });
});
