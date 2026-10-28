
import { useState } from "react";

const sampleMyListings = [
  {
    id: 1,
    title: "Scientific Calculator",
    category: "Electronics",
    itemCondition: "Good",
    startingPrice: 500,
    currentHighestBid: 650,
    status: "ACTIVE",
  },
  {
    id: 2,
    title: "Engineering Drawing Kit",
    category: "Stationery",
    itemCondition: "Like New",
    startingPrice: 250,
    currentHighestBid: null,
    status: "ACTIVE",
  },
];

function MyListings({ onViewListing }) {
  const [listings, setListings] = useState(sampleMyListings);

  function handleCancel(listingId) {
    const confirmed = window.confirm(
      "Are you sure you want to cancel this listing?"
    );

    if (!confirmed) return;

    setListings((previous) =>
      previous.map((listing) =>
        listing.id === listingId
          ? { ...listing, status: "CANCELLED" }
          : listing
      )
    );
  }

  return (
    <section className="my-listings-page">
      <h1>My Listings</h1>
      <p className="my-listings-subtitle">
        Manage the items you've put up for auction.
      </p>

      {listings.length === 0 ? (
        <p>You haven't created any listings yet.</p>
      ) : (
        <div className="my-listings-list">
          {listings.map((listing) => (
            <article className="my-listing-card" key={listing.id}>
              <div className="my-listing-info">
                <h3>{listing.title}</h3>

                <p>
                  {listing.category} · {listing.itemCondition}
                </p>

                <p>
                  Starting price: ₹{listing.startingPrice}
                </p>

                <p>
                  Highest bid: ₹
                  {listing.currentHighestBid ??
                    listing.startingPrice}
                </p>

                <span
                  className={`listing-status ${
                    listing.status === "ACTIVE"
                      ? "status-active"
                      : "status-cancelled"
                  }`}
                >
                  {listing.status}
                </span>
              </div>

              <div className="my-listing-actions">
                <button
                  type="button"
                  className="view-button"
                  onClick={() => onViewListing(listing.id)}
                >
                  View Details
                </button>

                {listing.status === "ACTIVE" && (
                  <button
                    type="button"
                    className="cancel-button"
                    onClick={() => handleCancel(listing.id)}
                  >
                    Cancel Listing
                  </button>
                )}
              </div>
            </article>
          ))}
        </div>
      )}
    </section>
  );
}

export default MyListings;
