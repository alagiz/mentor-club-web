import styled from "styled-components";

const gradientStartColor = "black";
const gradientEndColor = "#111f60";

export const LoginWrapper = styled.div`
  /* stylelint-disable-next-line */
  background: linear-gradient(
    45deg,
    ${gradientStartColor},
    ${gradientEndColor}
  );

  /* stylelint-disable-next-line */
  width: 100vw;

  /* stylelint-disable-next-line */
  height: 100vh;
`;
