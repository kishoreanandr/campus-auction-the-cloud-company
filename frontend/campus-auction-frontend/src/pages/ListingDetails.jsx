
import { useCallback, useEffect, useState } from "react";
import {
  getBidHistory,
  getImageUrl,
  getListing,
  getListingImages,
  placeBid,
  uploadListingImage,
  updateListingStatus,
} from "../services/api";

function ListingDetails({ listingId, user, onBack }) {
  const [listing, setListing] = useState(null);
  const [images, setImages] = useState([]);
  const [bids, setBids] = useState([]);
  const [bidAmount, setBidAmount] = useState("");
  const [imageFiles, setImageFiles] = useState([]);
  const [loading, setLoading] = useState(true);
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState("");
  const [message, setMessage] = useState("");

  const loadDetails = useCallback(async () => {
    try {
      const [listingData, imageData, bidData] = await Promise.all([
        getListing(listingId),
        getListingImages(listingId),
        getBidHistory(listingId),
      ]);
      setListing(listingData);
      setImages(imageData);
      setBids(bidData);
    } catch (requestError) {
      setError(requestError.message);
    } finally {
      setLoading(false);
    }
  }, [listingId]);

  useEffect(() => {
    let active = true;
    Promise.all([
      getListing(listingId),
      getListingImages(listingId),
      getBidHistory(listingId),
    ])
      .then(([listingData, imageData, bidData]) => {
        if (active) {
          setListing(listingData);
          setImages(imageData);
          setBids(bidData);
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
  }, [listingId]);

  if (loading) {
    return <p role="status">Loading listing…</p>;
  }
  if (error && !listing) {
    return (
      <section>
        <button className="back-button" onClick={onBack}>← Back to Listings</button>
        <p className="form-message form-error" role="alert">{error}</p>
      </section>
    );
  }
  if (!listing) {
    return <p>Listing not found.</p>;
  }

  const currentPrice =
    listing.currentHighestBid ?? listing.startingPrice;
  const minimumBid =
    listing.currentHighestBid == null
      ? Number(listing.startingPrice)
      : Number(currentPrice) + Number(listing.minimumIncrement);

  async function handleBidSubmit(event) {
    event.preventDefault();

    const amount = Number(bidAmount);

    if (!Number.isFinite(amount) || amount < minimumBid) {
      alert(`Your bid must be at least ₹${minimumBid}.`);
      return;
    }

    setSubmitting(true);
    setError("");
    setMessage("");
    try {
      await placeBid(listing.id, amount);
      setBidAmount("");
      setMessage("Your bid was submitted.");
      await loadDetails();
    } catch (requestError) {
      setError(requestError.message);
    } finally {
      setSubmitting(false);
    }
  }

  async function handleStatusAction(action) {
    setSubmitting(true);
    setError("");
    setMessage("");
    try {
      await updateListingStatus(listing.id, action);
      setMessage(action === "approve" ? "Winning bid approved." : "Winning bid rejected.");
      await loadDetails();
    } catch (requestError) {
      setError(requestError.message);
    } finally {
      setSubmitting(false);
    }
  }

  async function handleImageUpload(event) {
    event.preventDefault();
    setSubmitting(true);
    setError("");
    setMessage("");

    try {
      for (const image of imageFiles) {
        await uploadListingImage(listing.id, image);
      }
      setImageFiles([]);
      setMessage("Listing photos uploaded.");
      await loadDetails();
    } catch (requestError) {
      setError(requestError.message);
    } finally {
      setSubmitting(false);
    }
  }

  const canManageListing =
    user?.role === "BIDDER" && user.id === listing.sellerId;
  const canPlaceBid = user?.role === "USER" && user.id !== listing.sellerId;
  const isAuctionOpen =
    listing.status === "ACTIVE" && new Date(listing.closingAt) > new Date();

  return (
    <section className="details-page">
      <button className="back-button" onClick={onBack}>
        ← Back to Listings
      </button>

      <div className="details-layout">
        <div className="details-image">
          {images.length > 0 ? (
            images.map((image) => (
              <img
                key={image.id}
                src={getImageUrl(image.imagePath)}
                alt={listing.title}
              />
            ))
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

          <p><strong>Status:</strong> {listing.status}</p>
          {error && <p className="form-message form-error" role="alert">{error}</p>}
          {message && <p className="form-message" role="status">{message}</p>}

          {canManageListing && listing.status === "ACTIVE" && images.length < 5 && (
            <form className="listing-form photo-upload-form" onSubmit={handleImageUpload}>
              <label htmlFor="detail-images">Add listing photos (up to 5 total)</label>
              <input
                id="detail-images"
                type="file"
                accept="image/jpeg,image/png,image/webp"
                multiple
                onChange={(event) => {
                  const selectedImages = Array.from(event.target.files || []);
                  if (selectedImages.length + images.length > 5) {
                    setError("A listing can have at most 5 photos.");
                    setImageFiles([]);
                    event.target.value = "";
                    return;
                  }
                  setImageFiles(selectedImages);
                  setError("");
                }}
              />
              <button
                type="submit"
                className="view-button"
                disabled={submitting || imageFiles.length === 0}
              >
                Upload photos
              </button>
            </form>
          )}

          {canPlaceBid && isAuctionOpen && (
            <form onSubmit={handleBidSubmit} className="bid-form">
              <label htmlFor="bidAmount">Your bid (₹)</label>
              <input
                id="bidAmount"
                type="number"
                min={minimumBid}
                step="0.01"
                value={bidAmount}
                onChange={(event) => setBidAmount(event.target.value)}
                placeholder="Enter your bid"
                required
              />
              <button type="submit" className="view-button" disabled={submitting}>
                {submitting ? "Submitting…" : "Place Bid"}
              </button>
            </form>
          )}

          {!user && <p>Sign in as a student to place a bid.</p>}
          {user?.role === "BIDDER" && user.id === listing.sellerId && (
            <p>You posted this auction and cannot bid on your own item.</p>
          )}

          {canManageListing && listing.status === "PENDING_SELLER_APPROVAL" && (
            <div className="status-actions">
              <button
                type="button"
                className="view-button"
                disabled={submitting}
                onClick={() => handleStatusAction("approve")}
              >
                Approve winning bid
              </button>
              <button
                type="button"
                className="cancel-button"
                disabled={submitting}
                onClick={() => handleStatusAction("reject")}
              >
                Reject winning bid
              </button>
            </div>
          )}

          {user?.role === "ADMIN" && listing.status === "ACTIVE" && !isAuctionOpen && (
            <button
              type="button"
              className="view-button"
              disabled={submitting}
              onClick={() => handleStatusAction("close")}
            >
              Close auction
            </button>
          )}

          <section className="bid-history">
            <h2>Bid history</h2>
            {bids.length === 0 ? (
              <p>No bids have been placed.</p>
            ) : (
              <ul>
                {bids.map((bid) => (
                  <li key={bid.id}>
                    {bid.bidderName}: ₹{bid.amount} ·{" "}
                    {new Date(bid.bidTime).toLocaleString()}
                  </li>
                ))}
              </ul>
            )}
          </section>
        </div>
      </div>
    </section>
  );
}

export default ListingDetails;
