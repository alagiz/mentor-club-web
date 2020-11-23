import React from "react";
import FieldDropdownView from "../View/FieldDropdownView";
import { IFieldDropdownProps } from "./IFieldDropdownProps";

const FieldDropdown: React.FC<IFieldDropdownProps> = props => (
  <FieldDropdownView {...props} />
);

export default FieldDropdown;
