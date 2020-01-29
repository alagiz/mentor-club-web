import { shallow } from "enzyme";
import React from "react";
import CardDataItemView from "./CardDataItemView";

describe("Given a CardDataItemView", () => {
  describe("when rendering", () => {
    it("should have CardDataItemView matching snapshot", () => {
      const cardDataItemView = shallow(
        <CardDataItemView
          label={""}
          valueContainer={{ value: "", customClass: "" }}
        />
      );

      expect(cardDataItemView).toMatchSnapshot();
    });
  });
});
