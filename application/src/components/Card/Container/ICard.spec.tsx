import React from "react";
import { shallow } from "enzyme";
import Card from "./Card";

describe("Given a Card", () => {
  describe("when rendering", () => {
    it("should have Card matching snapshot", () => {
      const card = shallow(
        <Card cardDataItems={[]} cardBodyFooter={""} cardTitle={""} />
      );

      expect(card).toMatchSnapshot();
    });
  });
});
