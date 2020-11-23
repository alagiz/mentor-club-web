import React from "react";
import { Select } from "antd";
import { Styled } from "../Styled";
import translator from "../../../config/i18next";
import { IFieldDropdownProps } from "../Container/IFieldDropdownProps";

const FieldDropdownView: React.FC<IFieldDropdownProps> = ({
  labelText,
  optionValues,
  loading,
  value,
  defaultValue,
  onChange
}) => {
  const createOptions = (options: string[] | null) => {
    if (!options || !options.length) {
      return undefined;
    }

    return options.map((value: string) => (
      <Select.Option key={value} value={value}>
        {value}
      </Select.Option>
    ));
  };

  return (
    <>
      <Styled.Label>{labelText}</Styled.Label>
      <Styled.Dropdown
        showSearch
        defaultValue={defaultValue}
        loading={loading}
        disabled={!optionValues.length}
        placeholder={`${translator.t(
          "dropdown.placeholderPrefix"
        )} ${labelText}`}
        value={value ? value : undefined}
        onChange={onChange}
      >
        {createOptions(optionValues)}
      </Styled.Dropdown>
    </>
  );
};

export default FieldDropdownView;
