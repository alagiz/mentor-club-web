export interface INotificationListProps {
  notifications: any;
  deleteNotification: (notificationId: number) => void;
  notificationRenderer: (payload: any) => JSX.Element;
}
