import axios, { AxiosError, AxiosResponse } from "axios";
import settings from "../../config/settings";
import { store } from "../../store";
import { logout } from "../../state/Authentication/actions";
import { getToken } from "../UserManager/UserManager";

const handleError = (error: any) => {
  if (!error.response) {
    throw new Error("An unexpected error occurred");
  }

  switch (error.response.status) {
    case 401:
      store.dispatch(logout());
      throw new Error("An authorization error occurred");
    default:
      throw new Error("An unexpected error occurred");
  }
};

// @ts-ignore
const baseUrl = window._env_ ? window._env_.BACKEND_URL : settings.apiBaseUrl;
const getUrl = (endpoint: string): string => baseUrl + endpoint;

export const get = async (endpoint: string, body?: any): Promise<string[]> => {
  return await axios
    .get(getUrl(endpoint), {
      headers: {
        "Content-Type": "application/json; charset=utf-8",
        Authorization: `Bearer ${getToken()}`
      },
      params: body
    })
    .then((response: AxiosResponse) => {
      return response.data;
    })
    .catch((error: AxiosError) => handleError(error));
};

export const post = async <T>(
  endpoint: string,
  request?: T
): Promise<string[] | undefined> => {
  return await axios
    .post(getUrl(endpoint), request, {
      headers: {
        "Content-Type": "application/json; charset=utf-8",
        Authorization: `Bearer ${getToken()}`
      }
    })
    .then((response: AxiosResponse) => {
      return response.data;
    })
    .catch((error: AxiosError) => handleError(error));
};
