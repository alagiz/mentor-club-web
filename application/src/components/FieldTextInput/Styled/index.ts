import styled from "styled-components";
import { Input } from "antd";
const { TextArea } = Input;

const Label = styled.label`
  display: block;
  width: 100%;
  font-size: 20px;
  margin: 5px 0;
  color: white;
`;

const FieldTextArea = styled(TextArea)`
  margin-bottom: 0;
  border-radius: 0;
`;

const FieldInput = styled(Input)`
  margin-bottom: 0;
  border-radius: 0;
`;

export const Styled = {
  FieldTextArea,
  FieldInput,
  Label
};
