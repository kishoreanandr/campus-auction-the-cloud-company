
import { useState } from "react";
import Navbar from "./components/Navbar";
import Home from "./pages/Home";
import ListingDetails from "./pages/ListingDetails";
import CreateListing from "./pages/CreateListing";
import AuthPage from "./pages/AuthPage";
import "./App.css";

import MyListings from "./pages/MyListings";
import { clearAuth, getStoredAuth, storeAuth } from "./services/api";

function App() {
  const [currentPage, setCurrentPage] = useState("home");
  const [selectedListingId, setSelectedListingId] = useState(null);
  const [auth, setAuth] = useState(getStoredAuth);

  function handleViewListing(listingId) {
    setSelectedListingId(listingId);
    setCurrentPage("details");
  }

  function handleBackToHome() {
    setCurrentPage("home");
    setSelectedListingId(null);
  }

  function handleAuthentication(authResponse) {
    storeAuth(authResponse);
    setAuth(authResponse);
    setCurrentPage("home");
  }

  function handleLogout() {
    clearAuth();
    setAuth(null);
    setCurrentPage("home");
  }

  return (
    <>
      <Navbar
        user={auth?.user}
        onLogout={handleLogout}
        onNavigate={(page) => {
          setSelectedListingId(null);
          setCurrentPage(page);
        }}
      />

      <main className="page-content">
        {currentPage === "home" && (
          <Home onViewListing={handleViewListing} />
        )}

        {currentPage === "details" && (
          <ListingDetails
            listingId={selectedListingId}
            user={auth?.user}
            onBack={handleBackToHome}
          />
        )}

        {currentPage === "my-listings" && (
          <MyListings onViewListing={handleViewListing} />
        )}

        {currentPage === "create" && (
          <CreateListing
            onBack={handleBackToHome}
            onCreated={handleBackToHome}
          />
        )}

        {currentPage === "auth" && (
          <AuthPage onAuthenticated={handleAuthentication} />
        )}
      </main>
    </>
  );
}

export default App;
