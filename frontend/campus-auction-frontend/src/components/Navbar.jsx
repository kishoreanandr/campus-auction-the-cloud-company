
function Navbar({ onNavigate }) {
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

        <button onClick={() => onNavigate("my-listings")}>
          My Listings
        </button>

        <button
          className="create-button"
          onClick={() => onNavigate("create")}
        >
          + Create Listing
        </button>
      </nav>
    </header>
  );
}

export default Navbar;
