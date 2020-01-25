import { TransitionStatus } from "react-transition-group/Transition";

export interface INotificationViewProps {
  onClose: () => void;
  transitionState: TransitionStatus;
}
