import React from "react";
import { Styled } from "../Styled";
import { IFieldInputProps } from "./IFieldInputProps";

const FieldInputView: React.FC<IFieldInputProps> = ({
  labelText,
  value,
  onChange,
  isTextArea,
  numberOfLinesForTextArea
}) => {
  return (
    <>
      <Styled.Label>{labelText}</Styled.Label>
      {isTextArea && (
        <Styled.FieldTextArea
          rows={numberOfLinesForTextArea}
          value={value}
          onChange={onChange}
        />
      )}
      {!isTextArea && (
        <Styled.FieldInput type="text" value={value} onChange={onChange} />
      )}
    </>
  );
};

export default FieldInputView;
