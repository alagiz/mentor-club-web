import React from "react";
import { shallow } from "enzyme";
import FieldInputView from "./FieldInputView";

describe("Given FieldInputView", () => {
  describe("when rendering", () => {
    it("should have FieldInputView matching snapshot", () => {
      const component = shallow(
        <FieldInputView labelText={"fakeInput"} onChange={jest.fn()} />
      );

      expect(component).toMatchSnapshot();
    });
  });
});
