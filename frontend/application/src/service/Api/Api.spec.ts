import axios from "axios";

import { get, post } from "./Api";

jest.mock("axios");

describe("Given api calls", () => {
  const mockedAxios = axios as jest.Mocked<typeof axios>;
  const url = "http://test/data";

  describe("get", () => {
    describe("with working API", () => {
      const expectedData = { scannerIds: ["scannerIdA", "scannerIdB"] };
      const expectedResponse = { data: expectedData };

      beforeEach(() => {
        mockedAxios.get.mockImplementationOnce(() =>
          Promise.resolve(expectedResponse)
        );
      });

      it("should return a list of scanner ids if status code is 200", async () => {
        const results = await get(url);
        expect(results).toEqual(expectedData);
      });
    });

    describe("with failing API and status code 401", () => {
      const expectedResponse = { status: 401 };

      beforeEach(() => {
        mockedAxios.get.mockImplementationOnce(() =>
          Promise.reject(expectedResponse)
        );
      });

      it("should return an error if status code is not ok", () => {
        return expect(get(url)).rejects.toEqual(
          Error("An unexpected error occurred")
        );
      });
    });

    describe("with failing API and status code 500", () => {
      const expectedResponse = { status: 500 };

      beforeEach(() => {
        mockedAxios.get.mockImplementationOnce(() =>
          Promise.reject(expectedResponse)
        );
      });

      it("should return an error if status code is not ok", () => {
        return expect(get(url)).rejects.toEqual(
          Error("An unexpected error occurred")
        );
      });
    });
  });

  describe("post", () => {
    const requestBody = { id: "1" };

    describe("with working API", () => {
      const expectedData = { scannerIds: ["scannerIdA", "scannerIdB"] };
      const expectedResponse = { data: expectedData };

      beforeEach(() => {
        mockedAxios.post.mockImplementationOnce(() =>
          Promise.resolve(expectedResponse)
        );
      });

      it("should return a list of scanner ids if status code is 200", async () => {
        const results = await post(url, requestBody);
        expect(results).toEqual(expectedData);
      });
    });

    describe("with failing API and status code 401", () => {
      const expectedResponse = { status: 401 };

      beforeEach(() => {
        mockedAxios.post.mockImplementationOnce(() =>
          Promise.reject(expectedResponse)
        );
      });

      it("should return an error if status code is not ok", () => {
        return expect(post(url, requestBody)).rejects.toEqual(
          Error("An unexpected error occurred")
        );
      });
    });

    describe("with failing API and status code 500", () => {
      const expectedResponse = { status: 500 };

      beforeEach(() => {
        mockedAxios.post.mockImplementationOnce(() =>
          Promise.reject(expectedResponse)
        );
      });

      it("should return an error if status code is not ok", () => {
        return expect(post(url, requestBody)).rejects.toEqual(
          Error("An unexpected error occurred")
        );
      });
    });
  });
});
