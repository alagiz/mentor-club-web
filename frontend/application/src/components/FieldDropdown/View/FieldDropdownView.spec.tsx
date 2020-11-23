import React from "react";
import { shallow } from "enzyme";
import FieldDropdownView from "./FieldDropdownView";

describe("Given FieldDropdownView", () => {
  describe("when rendering", () => {
    it("should have FieldDropdownView matching snapshot", () => {
      const component = shallow(
        <FieldDropdownView
          labelText={"fakeLabel"}
          optionValues={["1", "2"]}
          onChange={jest.fn()}
        />
      );

      expect(component).toMatchSnapshot();
    });
  });
});
