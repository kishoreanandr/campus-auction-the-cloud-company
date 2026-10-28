
import { useEffect, useState } from "react";
import ListingCard from "../components/ListingCard";
import { getListings } from "../services/api";

function Home({ onViewListing }) {
  const [search, setSearch] = useState("");
  const [category, setCategory] = useState("All");
  const [listings, setListings] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  const categories = [
    "All",
    "Electronics",
    "Stationery",
    "Books",
    "Transport",
    "Others",
  ];

  useEffect(() => {
    let active = true;
    getListings({ category, search })
      .then((results) => {
        if (active) {
          setListings(results);
          setError("");
        }
      })
      .catch((requestError) => {
        if (active) {
          setError(requestError.message);
        }
      })
      .finally(() => {
        if (active) {
          setLoading(false);
        }
      });

    return () => {
      active = false;
    };
  }, [category, search]);

  return (
    <section className="home-page">
      <div className="home-header">
        <h1>Find What You Need on Campus</h1>
        <p>Buy and sell items with your fellow students.</p>
      </div>

      <div className="listing-filters">
        <input
          type="text"
          placeholder="Search for books, calculators..."
          value={search}
          onChange={(event) => setSearch(event.target.value)}
        />

        <select
          value={category}
          onChange={(event) => setCategory(event.target.value)}
        >
          {categories.map((item) => (
            <option key={item} value={item}>
              {item}
            </option>
          ))}
        </select>
      </div>

      {error && <p className="form-message form-error" role="alert">{error}</p>}
      {loading ? (
        <p role="status">Loading listings…</p>
      ) : (
        <div className="listing-grid">
          {listings.length > 0 ? (
            listings.map((listing) => (
            <ListingCard
              key={listing.id}
              listing={listing}
              onView={onViewListing}
            />
          ))
          ) : !error ? (
            <p>No listings found. Try another search.</p>
          ) : null}
        </div>
      )}
    </section>
  );
}

export default Home;
