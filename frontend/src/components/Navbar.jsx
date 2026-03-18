
import { Link, useLocation } from "react-router-dom";
import "../styles/Navbar.css";


const Navbar = ({ onProfileToggle }) => {
  const location = useLocation();

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
      </div>
      <div className="navbar-right">
        <button className="profile-btn" onClick={onProfileToggle}>
            ☰
        </button>
      </div>
    </nav>
  );
};

export default Navbar;
