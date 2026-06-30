import { NavLink } from "react-router-dom";
import {
  Home,
  MapPin,
  Gift,
  CalendarDays,
  Heart,
  MessageSquare,
  Search,
  Plane,
  UserCircle,
  ChevronDown,
  Menu,
} from "lucide-react";

import "../css/Navbar.css";

function Navbar() {
  return (
    <nav className="tripnest-navbar">
      <div className="tripnest-card">
        <div className="tripnest-top-row">
          <NavLink to="/" className="tripnest-brand">
            <div className="tripnest-brand-text">
              <h1>
                Trip<span>Nest</span>
              </h1>
              <p>Explore Beyond Limits</p>
            </div>
          </NavLink>

          <div className="tripnest-actions">
            <div className="tripnest-search">
              <Search size={24} />
              <input type="text" placeholder="Search destinations..." />
            </div>

            <NavLink to="/booking" className="tripnest-plan">
              <Plane size={23} />
              Plan Trip
            </NavLink>

            <NavLink to="/login" className="tripnest-login">
              <span className="tripnest-user">
                <UserCircle size={36} />
              </span>
              <span>Login / Register</span>
              <ChevronDown size={18} />
            </NavLink>
          </div>

          <div className="tripnest-mobile-icons">
            <button className="tripnest-icon-btn" type="button" aria-label="Search">
              <Search size={28} />
            </button>

            <button
              className="tripnest-icon-btn"
              type="button"
              data-bs-toggle="collapse"
              data-bs-target="#tripnestMobileMenu"
              aria-controls="tripnestMobileMenu"
              aria-expanded="false"
              aria-label="Toggle navigation"
            >
              <Menu size={30} />
            </button>
          </div>
        </div>

        <div
          className="tripnest-bottom-row collapse navbar-collapse"
          id="tripnestMobileMenu"
        >
          <ul className="tripnest-menu">
            <li>
              <NavLink to="/">
                <Home size={23} />
                Home
              </NavLink>
            </li>

            <li>
              <NavLink to="/destinations">
                <MapPin size={23} />
                Destinations
              </NavLink>
            </li>

            <li>
              <NavLink to="/packages">
                <Gift size={23} />
                Packages
              </NavLink>
            </li>

            <li>
              <NavLink to="/my-booking">
                <CalendarDays size={23} />
                My Bookings
              </NavLink>
            </li>

            <li>
              <NavLink to="/wishlist">
                <Heart size={23} />
                Wishlist
              </NavLink>
            </li>

            <li>
              <NavLink to="/feedback">
                <MessageSquare size={23} />
                Feedback
              </NavLink>
            </li>
          </ul>

          <div className="tripnest-mobile-buttons">
            <NavLink to="/booking" className="tripnest-plan">
              <Plane size={22} />
              Plan Trip
            </NavLink>

            <NavLink to="/login" className="tripnest-mobile-login">
              <UserCircle size={22} />
              Login / Register
            </NavLink>
          </div>
        </div>
      </div>
    </nav>
  );
}

export default Navbar;
