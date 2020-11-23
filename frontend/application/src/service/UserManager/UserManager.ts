const tokenStorageName = "user/token";
const thumbnailStorageName = "user/thumbnail";
const userNameStorageName = "user/username";
const displayNameStorageName = "user/displayName";

export function storeToken(token: string) {
  localStorage.setItem(tokenStorageName, token);
}

export function getToken(): string {
  return localStorage.getItem(tokenStorageName) as string;
}

export function storeThumbnail(value: string) {
  localStorage.setItem(thumbnailStorageName, value);
}

export function getThumbnail(): string {
  return localStorage.getItem(thumbnailStorageName) as string;
}

export function storeUsername(value: string) {
  localStorage.setItem(userNameStorageName, value);
}

export function getUsername(): string | null {
  return localStorage.getItem(userNameStorageName);
}

export function storeDisplayName(value: string) {
  localStorage.setItem(displayNameStorageName, value);
}

export function getDisplayName(): string | null {
  return localStorage.getItem(displayNameStorageName);
}

export function removeUserFromLocalStorage() {
  localStorage.removeItem(tokenStorageName);
  localStorage.removeItem(thumbnailStorageName);
  localStorage.removeItem(userNameStorageName);
  localStorage.removeItem(displayNameStorageName);
}
