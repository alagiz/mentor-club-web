export const APP_STARTED = "app/APP_STARTED";

export const appStarted = () => {
  return {
    type: APP_STARTED
  } as const;
};
