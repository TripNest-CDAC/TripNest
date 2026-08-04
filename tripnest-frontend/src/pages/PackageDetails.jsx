import { useEffect, useState } from "react";
import { Link, useParams } from "react-router-dom";
import { ArrowLeft, Building2, MapPin, Star } from "lucide-react";
import { useSelector } from "react-redux";
import packageService from "../services/packageService";
import transactionService from "../services/transactionService";
import "../css/Package.css";

function PackageDetails() {
  const { id } = useParams();
  const [travelPackage, setTravelPackage] = useState(null);
  const [trips, setTrips] = useState([]);
  const [rating, setRating] = useState({ averageRating: 0, reviewCount: 0 });
  const [error, setError] = useState("");
  const [message, setMessage] = useState("");
  const { isAuthenticated, role } = useSelector((state) => state.auth);

  useEffect(() => {
    packageService.get(id).then(setTravelPackage).catch((apiError) => {
      setError(apiError.response?.data?.message || "Unable to load this package.");
    });
    packageService.availableTrips().then((items) => setTrips(items.filter((trip) => String(trip.packageId) === String(id)))).catch(() => setTrips([]));
    transactionService.packageFeedbackSummary(id).then(setRating).catch(() => setRating({ averageRating: 0, reviewCount: 0 }));
  }, [id]);

  const addWishlist = async (tripId) => {
    try { await transactionService.addWishlist(tripId); setMessage("Trip added to your wishlist."); }
    catch (apiError) { setError(apiError.response?.data?.message || "Trip could not be added to wishlist."); }
  };

  return (
    <main className="packages-page">
      <Link to="/packages" className="back-link"><ArrowLeft size={18} /> Back to packages</Link>
      {error && <div className="form-alert error-alert">{error}</div>}
      {message && <div className="form-alert success-alert">{message}</div>}
      {!travelPackage && !error && <div className="package-loading">Loading package details…</div>}
      {travelPackage && (
        <section className="package-details-card">
          {travelPackage.thumbnailUrl && <div className="package-detail-image"><img src={travelPackage.thumbnailUrl} alt={`${travelPackage.packageName} destination`} onError={(event) => { event.currentTarget.parentElement.style.display = "none"; }} /></div>}
          <div className="package-detail-header"><span>{travelPackage.status}</span><strong>₹{Number(travelPackage.price).toLocaleString("en-IN")}</strong></div>
          <h1>{travelPackage.packageName}</h1>
          <p>{travelPackage.description || "A thoughtfully planned TripNest travel experience."}</p>
          <div className="package-rating"><Star size={19} fill="currentColor" /><strong>{Number(rating.averageRating).toFixed(1)} / 5</strong><span>{rating.reviewCount} review{rating.reviewCount === 1 ? "" : "s"}</span></div>
          <div className="detail-route"><MapPin size={22} /> <div><span>Route</span><strong>{travelPackage.source} to {travelPackage.destination}</strong></div></div>
          <div className="detail-route"><Building2 size={22} /> <div><span>Travel partner</span><strong>{travelPackage.companyName}</strong></div></div>
          <section className="package-departures"><h2>Available departures</h2>{trips.length === 0 ? <p>No upcoming departure is available for this package right now.</p> : trips.map((trip) => <div className="departure-row" key={trip.tripId}><div><strong>{trip.startDate} to {trip.endDate}</strong><span>{trip.seatsAvailable} seats remaining</span></div>{isAuthenticated && role === "TOURIST" ? <div><Link to={`/booking?tripId=${trip.tripId}`} className="package-primary-button">Book this trip</Link><button className="icon-text-button" onClick={() => addWishlist(trip.tripId)}>Save</button></div> : <Link to="/login" className="package-primary-button">Login to book</Link>}</div>)}</section>
        </section>
      )}
    </main>
  );
}

export default PackageDetails;
