
const API_BASE_URL = "http://localhost:8080/api";

export async function getListings({ category, search } = {}) {
  const params = new URLSearchParams();

  if (category && category !== "All") {
    params.append("category", category);
  }

  if (search?.trim()) {
    params.append("search", search.trim());
  }

  const queryString = params.toString();

  const url = queryString
    ? `${API_BASE_URL}/listings?${queryString}`
    : `${API_BASE_URL}/listings`;

  const response = await fetch(url);

  if (!response.ok) {
    throw new Error(`Failed to fetch listings. Status: ${response.status}`);
  }

  return response.json();
}
