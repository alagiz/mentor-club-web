import styled from "styled-components";
import { Select } from "antd";

const Label = styled.label`
  display: block;
  width: 100%;
  font-size: 20px;
  margin: 5px 0;
  color: white;
`;

const Dropdown = styled(Select)`
  width: 100%;
  margin-bottom: 10px;
  border-radius: 0;

  .ant-select-selection {
    border-radius: 0;
  }
`;

export const Styled = {
  Dropdown,
  Label
};
