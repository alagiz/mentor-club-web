import { mount } from "enzyme";
import React from "react";

const TestHook = ({ callback }: any) => {
  callback();
  return null;
};

export const testHook = (callback: any) => {
  mount(<TestHook callback={callback} />);
};
