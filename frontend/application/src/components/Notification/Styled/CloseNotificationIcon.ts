import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import styled from "styled-components";

const notificationIconMargin = "0.833rem";
const notificationIconSize = "12px";

export const CloseNotificationIcon = styled(FontAwesomeIcon)`
  color: grey;
  position: absolute;
  top: ${notificationIconMargin};
  right: ${notificationIconMargin};
  width: ${notificationIconSize};
  height: ${notificationIconSize};
  cursor: pointer;
`;
