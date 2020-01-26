import React from "react";
import { shallow } from "enzyme";
import CardDataItem from "./CardDataItem";

describe("Given a CardDataItem", () => {
  describe("when rendering", () => {
    it("should have CardDataItem matching snapshot", () => {
      const cardDataItem = shallow(<CardDataItem label={""} value={""} />);

      expect(cardDataItem).toMatchSnapshot();
    });
  });
});
