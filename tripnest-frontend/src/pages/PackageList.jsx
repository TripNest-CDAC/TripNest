import { useCallback, useEffect, useState } from "react";
import { useSelector } from "react-redux";
import { Link, useSearchParams } from "react-router-dom";
import { ArrowRight, MapPin } from "lucide-react";
import packageService, { packageImageUrl } from "../services/packageService";
import DestinationSearch from "../components/DestinationSearch";
import "../css/Package.css";

function PackageList() {
  const { role, isAuthenticated } = useSelector((state) => state.auth);
  const [searchParams] = useSearchParams();
  const availableOnly = searchParams.get("available") === "true";
  const [packages, setPackages] = useState([]);
  const [trips, setTrips] = useState([]);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const loadPackages = useCallback(async (destinationId = "") => {
    setLoading(true);
    setError("");
    try {
      const packageResult = (availableOnly || !isAuthenticated || role === "TOURIST")
        ? await packageService.available("", destinationId)
        : await packageService.list(destinationId ? "" : "", true);
      setPackages(packageResult);
      if (availableOnly || !isAuthenticated) {
        setTrips(await packageService.availableTrips());
      }
    } catch (apiError) {
      setError(apiError.response?.data?.message || "Unable to load travel packages.");
    } finally {
      setLoading(false);
    }
  }, [availableOnly, isAuthenticated]);

  useEffect(() => {
    const initialLoad = window.setTimeout(() => {
      loadPackages(searchParams.get("destinationId") || "");
    }, 0);

    return () => window.clearTimeout(initialLoad);
  }, [loadPackages, searchParams]);

  const destinationId = searchParams.get("destinationId") || "";
  const destinationLabel = searchParams.get("destination") || "";
  const selectDestination = (destination) => window.location.assign(`/packages?available=true&destinationId=${destination.id}&destination=${encodeURIComponent(destination.displayName)}`);

  return (
    <main className="packages-page">
      <header className="packages-hero">
        <div>
          <p>Travel packages</p>
          <h1>{destinationId ? `Trips to ${destinationLabel.split(",")[0]}` : availableOnly ? "Available trips and packages" : role === "COMPANY" ? "Your travel package collection" : "Choose your next escape"}</h1>
          <span>{role === "COMPANY" ? "Manage the packages created by your company." : "Browse active experiences from verified TripNest travel partners."}</span>
        </div>
        {role === "COMPANY" && <Link to="/manage-packages" className="package-primary-button">Manage packages <ArrowRight size={18} /></Link>}
      </header>

      {(role === "TOURIST" || availableOnly) && (
        <DestinationSearch className="package-destination-search" onSearch={selectDestination} initialDestinationId={destinationId} initialLabel={destinationLabel} />
      )}

      {error && <div className="form-alert error-alert">{error}</div>}
      {loading && <div className="package-loading">Loading travel packages…</div>}

      {!loading && !error && packages.length === 0 && (
        <div className="package-empty"><MapPin size={42} /><h2>No packages found</h2><p>{destinationId ? `No active trips are currently available for ${destinationLabel.split(",")[0]}.` : role === "COMPANY" ? "Create your first travel package to display it here." : "Choose a destination from the available suggestions."}</p></div>
      )}

      <section className="package-grid">
        {packages.map((travelPackage) => (
          <article className="package-card" key={travelPackage.packageId}>
            {travelPackage.thumbnailUrl && <div className="package-card-image"><img src={packageImageUrl(travelPackage.thumbnailUrl)} alt={`${travelPackage.packageName} destination`} onError={(event) => { event.currentTarget.parentElement.style.display = "none"; }} /></div>}
            <div className="package-card-top"><span>{travelPackage.status}</span><strong>₹{Number(travelPackage.price).toLocaleString("en-IN")}</strong></div>
            <h2>{travelPackage.packageName}</h2>
            <p>{travelPackage.description || "A thoughtfully planned TripNest travel experience."}</p>
            <div className="package-route"><MapPin size={17} /> {travelPackage.source} <ArrowRight size={16} /> {travelPackage.destination}</div>
            <small>By {travelPackage.companyName}</small>
            {trips.filter((trip) => trip.packageId === travelPackage.packageId).slice(0, 2).map((trip) => (
              <small className="available-trip" key={trip.tripId}>
                Trip: {trip.startDate} to {trip.endDate} · {trip.seatsAvailable} seats left
              </small>
            ))}
            <Link to={`/packages/${travelPackage.packageId}`} className="package-details-link">View package <ArrowRight size={17} /></Link>
          </article>
        ))}
      </section>
    </main>
  );
}

export default PackageList;
