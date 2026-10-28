
import { useState } from "react";
import Navbar from "./components/Navbar";
import Home from "./pages/Home";
import ListingDetails from "./pages/ListingDetails";
import CreateListing from "./pages/CreateListing";
import AuthPage from "./pages/AuthPage";
import "./App.css";

import MyListings from "./pages/MyListings";
import { clearAuth, getStoredAuth, storeAuth } from "./services/api";
import { useAuctionNotifications } from "./hooks/useAuctionNotifications";

function App() {
  const [currentPage, setCurrentPage] = useState("home");
  const [selectedListingId, setSelectedListingId] = useState(null);
  const [auth, setAuth] = useState(getStoredAuth);
  const {
    notifications,
    permission,
    enableNotifications,
    dismissNotification,
  } = useAuctionNotifications(auth?.user);

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
        notificationPermission={permission}
        onEnableNotifications={enableNotifications}
        onLogout={handleLogout}
        onNavigate={(page) => {
          setSelectedListingId(null);
          setCurrentPage(page);
        }}
      />

      <main className="page-content">
        <div className="notification-stack" aria-live="polite">
          {notifications.map((notification) => (
            <article className="notification-toast" key={notification.id}>
              <div>
                <strong>{notification.title}</strong>
                <p>{notification.body}</p>
              </div>
              <button
                type="button"
                aria-label="Dismiss notification"
                onClick={() => dismissNotification(notification.id)}
              >
                ×
              </button>
            </article>
          ))}
        </div>

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
