import styled from "styled-components";
import { boxShadow } from "../../../styles/variables/boxshadow";
import { TransitionStatus } from "react-transition-group/Transition";

interface INotificationBody {
  transitionState: TransitionStatus;
}

const padding = "2.5rem";
const marginBottom = "1.25rem";
const maxHeight = "15.833rem";

export const NotificationBody = styled.div<INotificationBody>`
  display: block;
  position: relative;
  background: white;
  width: 32em;
  box-shadow: ${boxShadow.medium};

  transition: 0.3s;

  ${({ transitionState }) => {
    switch (transitionState) {
      case "entering":
        return `
          right: -40rem;
          margin-bottom: ${marginBottom};
          max-height: ${maxHeight};
          padding: ${padding};
        `;
      case "exiting":
        return `
          margin-bottom: 0rem;
          max-height: 0;
          padding: 0 ${padding};
          opacity: 0;
        `;
      default:
        return `
          right: 0;
          margin-bottom: ${marginBottom};
          max-height: ${maxHeight};
          padding: ${padding};
          opacity: 1;
        `;
    }
  }}
`;
