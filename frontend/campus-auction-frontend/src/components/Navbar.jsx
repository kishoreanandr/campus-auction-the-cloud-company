
function Navbar({ user, onNavigate, onLogout }) {
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

        {user && (
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
