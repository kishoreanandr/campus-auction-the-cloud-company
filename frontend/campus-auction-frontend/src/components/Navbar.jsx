
function Navbar({
  user,
  notificationPermission,
  onEnableNotifications,
  onNavigate,
  onLogout,
}) {
  async function handleEnableNotifications() {
    try {
      const result = await onEnableNotifications();
      if (result === "denied") {
        window.alert("Notifications are blocked. Allow them in your browser's site settings.");
      } else if (result === "unsupported") {
        window.alert("This browser does not support desktop notifications.");
      }
    } catch (error) {
      window.alert(`Could not enable notifications: ${error.message}`);
    }
  }

  return (
    <header className="navbar">
      <button
        className="navbar-brand"
        onClick={() => onNavigate("home")}
      >
        Campus Auction
      </button>

      <nav className="navbar-links">
        <button onClick={() => onNavigate("home")}>
          Home
        </button>

        {user?.role === "BIDDER" && (
          <button onClick={() => onNavigate("my-listings")}>
            My Listings
          </button>
        )}

        {user?.role === "BIDDER" && (
          <button
            className="create-button"
            onClick={() => onNavigate("create")}
          >
            + Create Listing
          </button>
        )}

        {user ? (
          <>
            {["USER", "BIDDER"].includes(user.role) && (
              <button
                type="button"
                onClick={handleEnableNotifications}
                disabled={
                  notificationPermission === "granted"
                  || notificationPermission === "denied"
                  || notificationPermission === "unsupported"
                }
                title={
                  notificationPermission === "granted"
                    ? "Browser notifications are enabled"
                    : notificationPermission === "denied"
                      ? "Allow notifications in your browser site settings"
                      : "Allow notifications for auction updates"
                }
              >
                {notificationPermission === "granted"
                  ? "Notifications on"
                  : notificationPermission === "denied"
                    ? "Notifications blocked"
                    : notificationPermission === "unsupported"
                      ? "Notifications unavailable"
                  : "Enable notifications"}
              </button>
            )}
            <span className="navbar-user">
              {user.name} ({user.role})
            </span>
            <button onClick={onLogout}>Log out</button>
          </>
        ) : (
          <button onClick={() => onNavigate("auth")}>Sign in</button>
        )}
      </nav>
    </header>
  );
}

export default Navbar;
