import { shallow, ShallowWrapper } from "enzyme";
import React from "react";
import { App } from "./App";
import AppView from "../View/AppView";

describe("Given an App", () => {
  let component: ShallowWrapper;
  const appStartedMock = jest.fn();

  beforeEach(() => {
    jest.spyOn(React, "useEffect").mockImplementation(f => f());
    component = shallow(<App appStarted={appStartedMock} logout={jest.fn()} />);
  });

  describe("when rendering", () => {
    it("should render AppView", () => {
      expect(component.find(AppView)).toHaveLength(1);
    });

    it("should call appStarted on mounting", () => {
      expect(appStartedMock).toHaveBeenCalled();
    });
  });
});
