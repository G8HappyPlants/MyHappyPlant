import React, { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import "../styles/Navbar.css";

const Navbar = ({ onProfileToggle }) => {
  const [search, setSearch] = useState("");
  const navigate = useNavigate();

  const handleSearch = (e) => {
    e.preventDefault();
    // TODO - search logic
  };

  return (
    <nav className="navbar">
      <div className="navbar-left">
        <Link to="/own-library" className="navbar-link">Own Library</Link>
        <Link to="/database" className="navbar-link">Database</Link>
        <form className="navbar-search" onSubmit={handleSearch}>
          <input
            type="text"
            placeholder="Search..."
            value={search}
            onChange={(e) => setSearch(e.target.value)}
          />
        </form>
      </div>
      <div className="navbar-right">
        <button className="profile-btn" onClick={onProfileToggle}>
          <span role="img" aria-label="profile">👤</span>
        </button>
      </div>
    </nav>
  );
};

export default Navbar;
