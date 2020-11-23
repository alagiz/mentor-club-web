import { shallow } from "enzyme";
import React from "react";
import CardView from "./CardView";

describe("Given a CardView", () => {
  describe("when rendering", () => {
    it("should have CardView matching snapshot", () => {
      const cardView = shallow(
        <CardView cardTitle={""} cardBodyFooter={""} cardDataItems={[]} />
      );

      expect(cardView).toMatchSnapshot();
    });
  });
});
