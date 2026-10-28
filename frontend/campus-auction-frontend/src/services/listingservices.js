
const API_URL = "http://localhost:8080/api/listings";

export async function getListings(category = "", search = "") {
  const params = new URLSearchParams();

  if (category) {
    params.append("category", category);
  }

  if (search) {
    params.append("search", search);
  }

  const response = await fetch(
    `${API_URL}?${params.toString()}`
  );

  if (!response.ok) {
    throw new Error("Failed to fetch listings");
  }

  return response.json();
}
