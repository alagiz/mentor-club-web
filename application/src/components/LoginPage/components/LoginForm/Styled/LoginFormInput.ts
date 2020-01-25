import styled from "styled-components";
import { borderRadius, borderWidth } from "styles/variables/borders";

const loginFormWidth = "20rem";

export const LoginFormInput = styled.input`
  display: block;
  width: ${loginFormWidth};
  border: grey ${borderWidth.xxsmall} solid;
  padding: 2px;
  border-radius: ${borderRadius.none};
  margin: 0 0 10px 0;
`;
