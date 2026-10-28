
import { useState } from "react";
import Navbar from "./components/Navbar";
import Home from "./pages/Home";
import "./App.css";

function App() {
  const [currentPage, setCurrentPage] = useState("home");

  function handleViewListing(listingId) {
    alert("You selected listing ID: " + listingId);
  }

  return (
    <>
      <Navbar onNavigate={setCurrentPage} />

      <main className="page-content">
        {currentPage === "home" && (
          <Home onViewListing={handleViewListing} />
        )}

        {currentPage === "my-listings" && (
          <h1>My Listings</h1>
        )}

        {currentPage === "create" && (
          <h1>Create a Listing</h1>
        )}
      </main>
    </>
  );
}

export default App;
