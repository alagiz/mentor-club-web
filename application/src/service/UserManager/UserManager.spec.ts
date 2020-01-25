import {
  storeToken,
  getToken,
  getThumbnail,
  storeThumbnail,
  storeUsername,
  getUsername,
  storeDisplayName,
  getDisplayName,
  removeUserFromLocalStorage
} from "./UserManager";

describe("User manager", () => {
  const token = "test";
  const thumbnail = "thumbnailTest";
  const username = "fakeUsername";
  const displayName = "fakeDisplayName";

  afterEach(() => {
    localStorage.clear();
  });

  it("stores a token", () => {
    storeToken(token);
    expect(localStorage.getItem("user/token")).toBe(token);
  });

  it("retrieves a token", () => {
    storeToken(token);
    expect(getToken()).toBe(token);
  });

  it("returns null when token not set", () => {
    expect(getToken()).toBe(null);
  });

  it("stores a thumbnail", () => {
    storeThumbnail(thumbnail);
    expect(localStorage.getItem("user/thumbnail")).toBe(thumbnail);
  });

  it("retrieves a thumbnail", () => {
    storeThumbnail(thumbnail);
    expect(getThumbnail()).toBe(thumbnail);
  });

  it("returns null when thumbnail not set", () => {
    expect(getThumbnail()).toBe(null);
  });

  it("stores a username", () => {
    storeUsername(username);
    expect(localStorage.getItem("user/username")).toBe(username);
  });

  it("retrieves a username", () => {
    storeUsername(username);
    expect(getUsername()).toBe(username);
  });

  it("returns null when username not set", () => {
    expect(getUsername()).toBe(null);
  });

  it("stores a displayName", () => {
    storeDisplayName(displayName);
    expect(localStorage.getItem("user/displayName")).toBe(displayName);
  });

  it("retrieves a displayName", () => {
    storeDisplayName(displayName);
    expect(getDisplayName()).toBe(displayName);
  });

  it("returns null when thumbnail not set", () => {
    expect(getDisplayName()).toBe(null);
  });

  it("clears all user data when calling the clear function", () => {
    storeToken(token);
    storeThumbnail(thumbnail);
    storeUsername(username);
    storeDisplayName(displayName);

    removeUserFromLocalStorage();

    expect(getToken()).toBe(null);
    expect(getThumbnail()).toBe(null);
    expect(getUsername()).toBe(null);
    expect(getDisplayName()).toBe(null);
  });
});
