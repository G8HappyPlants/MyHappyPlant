
import React, { useState } from "react";
import { Link, useNavigate, useLocation } from "react-router-dom";
import "../styles/Navbar.css";


const Navbar = ({ onProfileToggle }) => {
  const [search, setSearch] = useState("");
  const navigate = useNavigate();
  const location = useLocation();

  const handleSearch = (e) => {
    e.preventDefault();
    // TODO - search logic
  };

  return (
    <nav className="navbar">
      <div className="navbar-left">
        <Link
          to="/user-library"
          className={`navbar-link${location.pathname === "/user-library" ? " active" : ""}`}
        >
          User Library
        </Link>
        <Link
          to="/species"
          className={`navbar-link${location.pathname === "/species" ? " active" : ""}`}
        >
          Species
        </Link>
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
