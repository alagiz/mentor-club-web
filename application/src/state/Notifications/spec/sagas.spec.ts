import { delay, put, take, fork, select } from "redux-saga/effects";
import {
  createNotification,
  displayNextNotification,
  listenToStatusNotifications
} from "../sagas";
import {
  UPDATE_MENTOR_REQUEST_STATUS,
  updateMentorRequestStatus
} from "../../MentorRequests/actions";
import {
  createMentorRequestUpdateNotification,
  deleteNotification,
  showNotification
} from "../actions";
import {
  selectNumberOfVisibleNotifications,
  selectOldestHiddenNotificationId
} from "../selectors";

const createMentorRequestUpdateAction = (visible: boolean) =>
  createMentorRequestUpdateNotification(1, "1", "DONE", visible);

describe("Notifications sagas", () => {
  describe("createNotification saga", () => {
    describe("when displaying less than maximum number of notifications", () => {
      const iterator: any = createNotification(
        updateMentorRequestStatus("1", "DONE"),
        1
      );

      it("should select the number of visible notifications", () => {
        expect(iterator.next().value).toStrictEqual(
          select(selectNumberOfVisibleNotifications)
        );
      });

      it("should dispatch a createMentorRequestUpdateNotification action", () => {
        expect(iterator.next(0).value).toStrictEqual(
          put(createMentorRequestUpdateAction(true))
        );
      });

      it("should delay execution for 5000ms", () => {
        expect(iterator.next().value).toStrictEqual(delay(5000));
      });

      it("should dispatch a deleteNotification action", () => {
        expect(iterator.next().value).toStrictEqual(put(deleteNotification(1)));
      });

      it("should then end the saga", () => {
        expect(iterator.next().done).toBe(true);
      });
    });

    describe("when displaying the maximum number of notifications", () => {
      const iterator: any = createNotification(
        updateMentorRequestStatus("1", "DONE"),
        1
      );

      it("should select the number of visible notifications", () => {
        expect(iterator.next().value).toStrictEqual(
          select(selectNumberOfVisibleNotifications)
        );
      });

      it("should dispatch a createMentorRequestUpdateNotification action", () => {
        expect(iterator.next(4).value).toStrictEqual(
          put(createMentorRequestUpdateAction(false))
        );
      });

      it("should then end the saga", () => {
        expect(iterator.next().done).toBe(true);
      });
    });
  });

  describe("listenToStatusNotifications saga", () => {
    const iterator: any = listenToStatusNotifications();

    it("should listen for UPDATE_MENTOR_REQUEST_STATUS actions", () => {
      expect(iterator.next().value).toStrictEqual(
        take(UPDATE_MENTOR_REQUEST_STATUS)
      );
    });

    it("should fork a create notification action", () => {
      expect(
        iterator.next(createMentorRequestUpdateAction).value
      ).toStrictEqual(
        fork(createNotification as any, createMentorRequestUpdateAction, 0)
      );
    });

    it("should listen for another UPDATE_MENTOR_REQUEST_STATUS actions", () => {
      expect(iterator.next().value).toStrictEqual(
        take(UPDATE_MENTOR_REQUEST_STATUS)
      );
    });

    it("should fork a create notification action with incremented ID", () => {
      expect(
        iterator.next(createMentorRequestUpdateAction).value
      ).toStrictEqual(
        fork(createNotification as any, createMentorRequestUpdateAction, 1)
      );
    });
  });

  describe("displayNextNotification saga", () => {
    describe("when displaying less than maximum number of notifications", () => {
      const iterator: any = displayNextNotification();

      it("should select the oldest hidden notification ID", () => {
        expect(iterator.next().value).toStrictEqual(
          select(selectOldestHiddenNotificationId)
        );
      });

      it("should select the number of visible notifications", () => {
        expect(iterator.next(1).value).toStrictEqual(
          select(selectNumberOfVisibleNotifications)
        );
      });

      it("should dispatch the show notification action", () => {
        expect(iterator.next(3).value).toStrictEqual(put(showNotification(1)));
      });

      it("should delay execution for 5000ms", () => {
        expect(iterator.next().value).toStrictEqual(delay(5000));
      });

      it("should dispatch the delete notification action", () => {
        expect(iterator.next(3).value).toStrictEqual(
          put(deleteNotification(1))
        );
      });

      it("should then end the saga", () => {
        expect(iterator.next().done).toBe(true);
      });
    });

    describe("when displaying the maximum number of notifications", () => {
      const iterator: any = displayNextNotification();

      it("should select the oldest hidden notification ID", () => {
        expect(iterator.next().value).toStrictEqual(
          select(selectOldestHiddenNotificationId)
        );
      });

      it("should select the number of visible notifications", () => {
        expect(iterator.next(1).value).toStrictEqual(
          select(selectNumberOfVisibleNotifications)
        );
      });

      it("should then end the saga", () => {
        expect(iterator.next(4).done).toBe(true);
      });
    });
  });
});
