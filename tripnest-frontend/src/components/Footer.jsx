import { Link } from "react-router-dom";
import "../css/Footer.css";

function Footer() {
  return (
    <footer className="footer">
      <div>
        <Link to="/" className="footer-brand">Trip<span>Nest</span></Link>
        <p>Secure travel experiences, built with Java, Spring Boot and React.</p>
      </div>
      <p className="footer-tagline">Explore beyond limits.</p>
    </footer>
  );
}

export default Footer;
