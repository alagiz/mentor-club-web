import styled from "styled-components";
import { Button } from "antd";
import { borderRadius } from "styles/variables/borders";

const buttonColor = "#161a37";
const buttonColorOnHover = "#282d65";
const buttonColorOnFocus = "#223965";

export const LoginFormButton = styled(Button)`
  width: 100%;
  border-radius: ${borderRadius.none};
  background: ${buttonColor};
  border: none;
  &:hover {
    background: ${buttonColorOnHover};
  }
  &:active {
    background: ${buttonColorOnFocus};
  }  
  &:focus {
    background: ${buttonColorOnFocus};
  }
`;
