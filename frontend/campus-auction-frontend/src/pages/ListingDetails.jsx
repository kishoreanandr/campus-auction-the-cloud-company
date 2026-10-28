
import { useState } from "react";

function ListingDetails({ listing, onBack }) {
  const [bidAmount, setBidAmount] = useState("");

  if (!listing) {
    return <p>Listing not found.</p>;
  }

  const currentPrice =
    listing.currentHighestBid ?? listing.startingPrice;

  function handleBidSubmit(event) {
    event.preventDefault();

    const amount = Number(bidAmount);

    if (!Number.isFinite(amount) || amount <= currentPrice) {
      alert(`Your bid must be greater than ₹${currentPrice}.`);
      return;
    }

    alert(`Your bid of ₹${amount} has been submitted (demo only).`);
    setBidAmount("");
  }

  return (
    <section className="details-page">
      <button className="back-button" onClick={onBack}>
        ← Back to Listings
      </button>

      <div className="details-layout">
        <div className="details-image">
          {listing.image ? (
            <img src={listing.image} alt={listing.title} />
          ) : (
            <span>No image available</span>
          )}
        </div>

        <div className="details-info">
          <span className="listing-category">
            {listing.category}
          </span>

          <h1>{listing.title}</h1>

          <p>
            <strong>Condition:</strong> {listing.itemCondition}
          </p>

          <p className="details-description">
            {listing.description ||
              "No description has been added for this item yet."}
          </p>

          <p>
            Starting price: ₹{listing.startingPrice}
          </p>

          <div className="details-price">
            Current highest bid: ₹{currentPrice}
          </div>

          <form onSubmit={handleBidSubmit} className="bid-form">
            <label htmlFor="bidAmount">Your bid (₹)</label>

            <input
              id="bidAmount"
              type="number"
              min={currentPrice + 1}
              step="0.01"
              value={bidAmount}
              onChange={(event) => setBidAmount(event.target.value)}
              placeholder="Enter your bid"
              required
            />

            <button type="submit" className="view-button">
              Place Bid
            </button>
          </form>

          <p className="demo-note">
            Demo only: bids are not saved yet.
          </p>
        </div>
      </div>
    </section>
  );
}

export default ListingDetails;
