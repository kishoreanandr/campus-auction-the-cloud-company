
import { useState } from "react";

function CreateListing({ onBack }) {
  const [formData, setFormData] = useState({
    title: "",
    category: "Books",
    description: "",
    itemCondition: "Good",
    startingPrice: "",
    minimumIncrement: "10",
    closingAt: "",
  });

  const [message, setMessage] = useState("");

  function handleChange(event) {
    const { name, value } = event.target;

    setFormData((previous) => ({
      ...previous,
      [name]: value,
    }));

    setMessage("");
  }

  function handleSubmit(event) {
    event.preventDefault();

    const startingPrice = Number(formData.startingPrice);
    const minimumIncrement = Number(formData.minimumIncrement);
    const closingDate = new Date(formData.closingAt);

    if (startingPrice <= 0) {
      setMessage("Starting price must be greater than zero.");
      return;
    }

    if (minimumIncrement <= 0) {
      setMessage("Minimum bid increment must be greater than zero.");
      return;
    }

    if (Number.isNaN(closingDate.getTime()) || closingDate <= new Date()) {
      setMessage("Please select a future closing date and time.");
      return;
    }

    console.log("Listing form data:", {
      ...formData,
      startingPrice,
      minimumIncrement,
    });

    setMessage("Form validated! Backend submission will be added next.");
  }

  return (
    <section className="create-listing-page">
      <button type="button" className="back-button" onClick={onBack}>
        ← Back to Listings
      </button>

      <div className="form-container">
        <h1>Create a Listing</h1>
        <p>Give your unused campus items a new home.</p>

        <form onSubmit={handleSubmit} className="listing-form">
          <label htmlFor="title">Item title</label>
          <input
            id="title"
            name="title"
            value={formData.title}
            onChange={handleChange}
            placeholder="e.g. Scientific Calculator"
            maxLength={150}
            required
          />

          <label htmlFor="category">Category</label>
          <select
            id="category"
            name="category"
            value={formData.category}
            onChange={handleChange}
          >
            <option>Books</option>
            <option>Electronics</option>
            <option>Stationery</option>
            <option>Transport</option>
            <option>Others</option>
          </select>

          <label htmlFor="itemCondition">Condition</label>
          <select
            id="itemCondition"
            name="itemCondition"
            value={formData.itemCondition}
            onChange={handleChange}
          >
            <option>New</option>
            <option>Like New</option>
            <option>Good</option>
            <option>Used</option>
          </select>

          <label htmlFor="description">Description</label>
          <textarea
            id="description"
            name="description"
            value={formData.description}
            onChange={handleChange}
            placeholder="Describe the item's condition and details"
            rows={4}
            maxLength={2000}
            required
          />

          <label htmlFor="startingPrice">Starting price (₹)</label>
          <input
            id="startingPrice"
            name="startingPrice"
            type="number"
            min="1"
            step="0.01"
            value={formData.startingPrice}
            onChange={handleChange}
            placeholder="500"
            required
          />

          <label htmlFor="minimumIncrement">
            Minimum bid increment (₹)
          </label>
          <input
            id="minimumIncrement"
            name="minimumIncrement"
            type="number"
            min="0.01"
            step="0.01"
            value={formData.minimumIncrement}
            onChange={handleChange}
            required
          />

          <label htmlFor="closingAt">Auction closing date and time</label>
          <input
            id="closingAt"
            name="closingAt"
            type="datetime-local"
            min={new Date(Date.now() - new Date().getTimezoneOffset() * 60000)
              .toISOString()
              .slice(0, 16)}
            value={formData.closingAt}
            onChange={handleChange}
            required
          />

          {message && (
            <p className="form-message" role="status">
              {message}
            </p>
          )}

          <button type="submit" className="submit-listing-button">
            Create Listing
          </button>
        </form>
      </div>
    </section>
  );
}

export default CreateListing;
