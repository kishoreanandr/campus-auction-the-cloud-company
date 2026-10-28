const configuredApiUrl = (import.meta.env.VITE_API_URL || "").replace(/\/+$/, "");
const requestBaseUrl = import.meta.env.DEV ? "" : configuredApiUrl;
const API_BASE_URL = requestBaseUrl
  ? `${requestBaseUrl}/api`
  : "/api";
const backendDisplayUrl = configuredApiUrl || "http://localhost:8080";
const AUTH_STORAGE_KEY = "campus-auction-auth";

export function getStoredAuth() {
  try {
    const storedAuth = sessionStorage.getItem(AUTH_STORAGE_KEY);
    return storedAuth ? JSON.parse(storedAuth) : null;
  } catch {
    sessionStorage.removeItem(AUTH_STORAGE_KEY);
    return null;
  }
}

export function storeAuth(auth) {
  sessionStorage.setItem(AUTH_STORAGE_KEY, JSON.stringify(auth));
}

export function clearAuth() {
  sessionStorage.removeItem(AUTH_STORAGE_KEY);
}

async function request(path, options = {}) {
  const headers = new Headers(options.headers || {});
  const auth = getStoredAuth();

  if (auth?.token) {
    headers.set("Authorization", `Bearer ${auth.token}`);
  }

  if (options.body && !(options.body instanceof FormData)) {
    headers.set("Content-Type", "application/json");
  }

  let response;
  try {
    response = await fetch(`${API_BASE_URL}${path}`, {
      ...options,
      headers,
    });
  } catch (error) {
    throw new Error(
      `Could not connect to the backend at ${backendDisplayUrl}. ` +
        "Check that it is running and VITE_API_URL is correct.",
      { cause: error },
    );
  }

  if (response.status === 204) {
    return null;
  }

  const contentType = response.headers.get("content-type") || "";
  const responseBody = contentType.includes("application/json")
    ? await response.json()
    : await response.text();

  if (!response.ok) {
    const message =
      typeof responseBody === "object" && responseBody !== null
        ? responseBody.message || responseBody.error || responseBody.detail
        : responseBody;
    throw new Error(message || `Request failed with status ${response.status}`);
  }

  return responseBody;
}

export function register(payload) {
  return request("/auth/register", {
    method: "POST",
    body: JSON.stringify(payload),
  });
}

export function login(payload) {
  return request("/auth/login", {
    method: "POST",
    body: JSON.stringify(payload),
  });
}

export function getListings({ category, search } = {}) {
  const params = new URLSearchParams();
  if (category && category !== "All") {
    params.set("category", category);
  }
  if (search?.trim()) {
    params.set("search", search.trim());
  }

  const query = params.toString();
  return request(`/listings/browse${query ? `?${query}` : ""}`);
}

export function getListing(listingId) {
  return request(`/listings/${listingId}`);
}

export function getMyListings() {
  return request("/listings/mine");
}

export function createListing(payload) {
  return request("/listings", {
    method: "POST",
    body: JSON.stringify(payload),
  });
}

export function cancelListing(listingId) {
  return request(`/listings/${listingId}`, { method: "DELETE" });
}

export function placeBid(listingId, amount) {
  return request(`/listings/${listingId}/bids`, {
    method: "POST",
    body: JSON.stringify({ amount }),
  });
}

export function getBidHistory(listingId) {
  return request(`/listings/${listingId}/bids`);
}

export function getListingImages(listingId) {
  return request(`/listings/${listingId}/images`);
}

export function uploadListingImage(listingId, file) {
  const formData = new FormData();
  formData.append("file", file);
  return request(`/listings/${listingId}/images`, {
    method: "POST",
    body: formData,
  });
}

export function updateListingStatus(listingId, action) {
  return request(`/listings/${listingId}/${action}`, { method: "POST" });
}

export function getImageUrl(imagePath) {
  if (!imagePath) {
    return "";
  }
  if (/^https?:\/\//i.test(imagePath)) {
    return imagePath;
  }
  const imageBaseUrl = import.meta.env.DEV ? "" : configuredApiUrl;
  return `${imageBaseUrl}/${imagePath.replace(/^\/+/, "")}`;
}
