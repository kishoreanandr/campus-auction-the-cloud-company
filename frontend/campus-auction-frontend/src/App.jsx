
import { useState } from "react";
import Navbar from "./components/Navbar";
import Home from "./pages/Home";
import ListingDetails from "./pages/ListingDetails";
import CreateListing from "./pages/CreateListing";
import "./App.css";

import MyListings from "./pages/MyListings";

function App() {
  const [currentPage, setCurrentPage] = useState("home");
  const [selectedListing, setSelectedListing] = useState(null);

  function handleViewListing(listing) {
    setSelectedListing(listing);
    setCurrentPage("details");
  }

  function handleBackToHome() {
    setCurrentPage("home");
    setSelectedListing(null);
  }

  return (
    <>
      <Navbar onNavigate={(page) => {
        setSelectedListing(null);
        setCurrentPage(page);
      }} />

      <main className="page-content">
        {currentPage === "home" && (
          <Home onViewListing={handleViewListing} />
        )}

        {currentPage === "details" && (
          <ListingDetails
            listing={selectedListing}
            onBack={handleBackToHome}
          />
        )}

        {currentPage === "my-listings" && (
          <MyListings onViewListing={(listingId)=>{
            alert("Selected listing Id: "+listingId);
          }}/>
        )}

        {currentPage === "create" && (
          <CreateListing onBack={handleBackToHome} />
        )}
      </main>
    </>
  );
}

export default App;
