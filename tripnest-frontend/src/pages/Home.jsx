import { Link } from "react-router-dom";
import {
  ArrowRight,
  Building2,
  CalendarDays,
  CircleCheck,
  Compass,
  MapPin,
  Plane,
  Sparkles,
} from "lucide-react";
import "../css/Home.css";

function Home() {
  return (
    <main className="home-page">
      <section className="home-hero">
        <div className="hero-copy">
          <div className="eyebrow">
            <CircleCheck size={17} />
            Your journey, beautifully planned
          </div>
          <h1>
            Discover places that
            <span> feel like home.</span>
          </h1>
          <p>
            From peaceful escapes to unforgettable adventures, TripNest helps
            you find, plan, and enjoy your perfect travel experience.
          </p>
          <div className="hero-actions">
            <Link to="/register" className="primary-action">
              Start exploring <ArrowRight size={19} />
            </Link>
            <Link to="/login" className="secondary-action">
              Plan your trip
            </Link>
          </div>
          <div className="hero-proof">
            <span><MapPin size={18} /> Handpicked destinations</span>
            <span><CalendarDays size={18} /> Easy trip planning</span>
            <span><Sparkles size={18} /> Memorable experiences</span>
          </div>
        </div>

        <div className="hero-panel">
          <div className="panel-orbit orbit-one" />
          <div className="panel-orbit orbit-two" />
          <div className="journey-card tourist-card">
            <div className="journey-icon"><Compass size={24} /></div>
            <div>
              <strong>Explore freely</strong>
              <span>Find your next favourite place</span>
            </div>
          </div>
          <div className="journey-card company-card">
            <div className="journey-icon"><Building2 size={24} /></div>
            <div>
              <strong>Travel partners</strong>
              <span>Experiences made for every traveller</span>
            </div>
          </div>
          <div className="security-seal">
            <Plane size={42} />
            <strong>TripNest</strong>
            <span>Explore beyond limits</span>
          </div>
        </div>
      </section>

      <section className="feature-strip">
        <article>
          <span>DISCOVER</span>
          <h2>Find your escape</h2>
          <p>Browse destinations and travel ideas curated for every kind of journey.</p>
        </article>
        <article>
          <span>PLAN</span>
          <h2>Make it yours</h2>
          <p>Build a trip that fits your dates, interests, and travel style.</p>
        </article>
        <article>
          <span>JOURNEY</span>
          <h2>Travel with confidence</h2>
          <p>Keep your plans in one place and focus on making great memories.</p>
        </article>
      </section>
    </main>
  );
}

export default Home;
