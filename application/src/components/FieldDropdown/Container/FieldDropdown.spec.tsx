import React from "react";
import { shallow } from "enzyme";
import FieldDropdown from "./FieldDropdown";
import FieldDropdownView from "../View/FieldDropdownView";

describe("Given FieldDropdown", () => {
  describe("when rendering", () => {
    it("should have FieldDropdown matching snapshot", () => {
      const component = shallow(
        <FieldDropdown
          labelText={"fakeLabel"}
          optionValues={["1", "2"]}
          onChange={jest.fn()}
        />
      );

      expect(component).toMatchSnapshot();
    });
  });

  describe("when changing the value", () => {
    it("should call the onChange function prop with the new value", () => {
      const mockOnChange = jest.fn();

      const component = shallow(
        <FieldDropdown
          labelText={"fakeLabel"}
          optionValues={["1", "2"]}
          onChange={mockOnChange}
        />
      );

      component
        .find(FieldDropdownView)
        .props()
        .onChange("testValue");

      expect(mockOnChange).toHaveBeenCalledWith("testValue");
    });
  });
});
