
import { useCallback, useEffect, useState } from "react";
import { cancelListing, getMyListings } from "../services/api";

function MyListings({ onViewListing }) {
  const [listings, setListings] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  const loadListings = useCallback(async () => {
    try {
      setListings(await getMyListings());
      setError("");
    } catch (requestError) {
      setError(requestError.message);
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    let active = true;
    getMyListings()
      .then((result) => {
        if (active) {
          setListings(result);
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
  }, []);

  async function handleCancel(listingId) {
    const confirmed = window.confirm(
      "Are you sure you want to cancel this listing?"
    );

    if (!confirmed) return;

    setError("");
    try {
      await cancelListing(listingId);
      await loadListings();
    } catch (requestError) {
      setError(requestError.message);
    }
  }

  return (
    <section className="my-listings-page">
      <h1>My Listings</h1>
      <p className="my-listings-subtitle">
        Manage the items you've put up for auction.
      </p>

      {error && <p className="form-message form-error" role="alert">{error}</p>}
      {loading ? (
        <p role="status">Loading your listings…</p>
      ) : listings.length === 0 ? (
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

                {listing.status === "ACTIVE" &&
                  listing.currentHighestBid == null &&
                  new Date(listing.closingAt) > new Date() && (
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
