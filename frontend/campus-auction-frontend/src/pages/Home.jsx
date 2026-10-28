
import { useState } from "react";
import ListingCard from "../components/ListingCard";

const sampleListings = [
  {
    id: 1,
    title: "Scientific Calculator",
    category: "Electronics",
    itemCondition: "Good",
    startingPrice: 500,
    currentHighestBid: null,
    image: "",
  },
  {
    id: 2,
    title: "Engineering Drawing Kit",
    category: "Stationery",
    itemCondition: "Like New",
    startingPrice: 250,
    currentHighestBid: 300,
    image: "",
  },
  {
    id: 3,
    title: "Data Structures Textbook",
    category: "Books",
    itemCondition: "Good",
    startingPrice: 200,
    currentHighestBid: null,
    image: "",
  },
  {
    id: 4,
    title: "Cycle",
    category: "Transport",
    itemCondition: "Used",
    startingPrice: 1500,
    currentHighestBid: null,
    image: "",
  },
];

function Home({ onViewListing }) {
  const [search, setSearch] = useState("");
  const [category, setCategory] = useState("All");

  const categories = [
    "All",
    "Electronics",
    "Stationery",
    "Books",
    "Transport",
  ];

  const filteredListings = sampleListings.filter((listing) => {
    const matchesSearch =
      listing.title.toLowerCase().includes(search.toLowerCase());

    const matchesCategory =
      category === "All" || listing.category === category;

    return matchesSearch && matchesCategory;
  });

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

      <div className="listing-grid">
        {filteredListings.length > 0 ? (
          filteredListings.map((listing) => (
            <ListingCard
              key={listing.id}
              listing={listing}
              onView={onViewListing}
            />
          ))
        ) : (
          <p>No listings found. Try another search.</p>
        )}
      </div>
    </section>
  );
}

export default Home;
