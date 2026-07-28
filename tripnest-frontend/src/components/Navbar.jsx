import { useDispatch, useSelector } from "react-redux";
import { NavLink, useNavigate } from "react-router-dom";
import {
  Home,
  MapPin,
  Gift,
  CalendarDays,
  Heart,
  MessageSquare,
  Plane,
  UserCircle,
  ChevronDown,
  Menu,
  LogOut,
  LayoutDashboard,
} from "lucide-react";

import { logout } from "../redux/authSlice";
import DestinationSearch from "./DestinationSearch";
import "../css/Navbar.css";

function Navbar() {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const { isAuthenticated, role, user } = useSelector((state) => state.auth);

  const dashboardPath =
    role === "ADMIN"
      ? "/admin-dashboard"
      : role === "COMPANY"
        ? "/company-dashboard"
        : "/user-dashboard";

  const handleLogout = () => {
    dispatch(logout());
    navigate("/");
  };
  const searchDestination = (destination) => navigate(`/packages?available=true&destinationId=${destination.id}&destination=${encodeURIComponent(destination.displayName)}`);

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
            <DestinationSearch className="navbar-destination-search" onSearch={searchDestination} />

            <NavLink to="/booking" className="tripnest-plan">
              <Plane size={23} />
              Plan Trip
            </NavLink>

            {isAuthenticated ? (
              <div className="signed-in-actions">
                <NavLink to={dashboardPath} className="tripnest-login">
                  <span className="tripnest-user"><LayoutDashboard size={22} /></span>
                  <span>{user?.username || role}</span>
                </NavLink>
                <button type="button" className="logout-button" onClick={handleLogout} aria-label="Logout">
                  <LogOut size={19} />
                </button>
              </div>
            ) : (
              <NavLink to="/login" className="tripnest-login">
                <span className="tripnest-user"><UserCircle size={30} /></span>
                <span>Login / Register</span>
                <ChevronDown size={18} />
              </NavLink>
            )}
          </div>

          <div className="tripnest-mobile-icons">
            <button className="tripnest-icon-btn" type="button" aria-label="Search">
              <MapPin size={28} />
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

            {isAuthenticated ? (
              <>
                <NavLink to={dashboardPath} className="tripnest-mobile-login">
                  <LayoutDashboard size={22} /> Dashboard
                </NavLink>
                <button type="button" className="tripnest-mobile-login" onClick={handleLogout}>
                  <LogOut size={22} /> Logout
                </button>
              </>
            ) : (
              <NavLink to="/login" className="tripnest-mobile-login">
                <UserCircle size={22} /> Login / Register
              </NavLink>
            )}
          </div>
        </div>
      </div>
    </nav>
  );
}

export default Navbar;
