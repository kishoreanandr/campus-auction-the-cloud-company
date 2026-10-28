
function ListingCard({ listing, onView }) {
  return (
    <div className="listing-card">
      <div className="listing-image">
        {listing.image ? (
          <img
            src={listing.image}
            alt={listing.title}
            onError={(event) => {
              event.currentTarget.hidden = true;
              event.currentTarget.parentElement.dataset.imageError = "true";
            }}
          />
        ) : (
          <span>No image available</span>
        )}
        {listing.image && (
          <span className="listing-image-fallback">Image unavailable</span>
        )}
      </div>

      <div className="listing-info">
        <span className="listing-category">
          {listing.category}
        </span>

        <h3>{listing.title}</h3>

        <p className="listing-condition">
          Condition: {listing.itemCondition}
        </p>

        <div className="listing-price">
          ₹{listing.currentHighestBid ?? listing.startingPrice}
        </div>

        <button
          className="view-button"
          onClick={() => onView(listing.id)}
        >
          View Details
        </button>
      </div>
    </div>
  );
}

export default ListingCard;
