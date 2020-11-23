import styled from "styled-components";

const buttonColor = "#0e0f16";

export const TopBar = styled.header`
  /* stylelint-disable-next-line */
  display: flex;
  justify-content: space-between;
  padding: 0 20px;
  height: 4rem;
  width: 100%;
  flex-shrink: 0;
  background: ${buttonColor};
`;
