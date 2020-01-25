import styled from "styled-components";
import { boxShadow } from "../../../../../styles/variables/boxshadow";
import { borderRadius } from "../../../../../styles/variables/borders";

export const LoginForm = styled.form`
  box-shadow: ${boxShadow.medium};
  padding: 20px;
  background: white;
  border-radius: ${borderRadius.none};
  margin-bottom: 10px;
`;
