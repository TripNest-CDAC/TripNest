import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { BriefcaseBusiness, CalendarDays, CircleDollarSign, Power } from "lucide-react";
import packageService from "../services/packageService";
import DashboardProfile from "../components/DashboardProfile";
import "../css/Dashboard.css";

function CompanyDashboard() {
  const [stats, setStats] = useState(null);
  useEffect(() => { packageService.companyDashboard().then(setStats).catch(() => setStats({})); }, []);
  const cards = [
    ["Total packages", stats?.totalPackages ?? "–", BriefcaseBusiness],
    ["Active packages", stats?.activePackages ?? "–", Power],
    ["Inactive packages", stats?.inactivePackages ?? "–", Power],
    ["Confirmed booking value", `₹${Number(stats?.bookingAmount ?? 0).toLocaleString("en-IN")}`, CircleDollarSign],
  ];
  return <main className="dashboard-page"><DashboardProfile title="Company command centre" description="Monitor your package catalogue, availability and confirmed booking value." />
    <section className="admin-summary">{cards.map(([label, value, Icon]) => <article key={label}><div className="summary-icon"><Icon size={24}/></div><span>{label}</span><strong>{value}</strong></article>)}</section>
    <section className="approval-section"><div className="section-title"><div><p>Operations</p><h2>Manage your travel business</h2></div><span>{stats?.upcomingTrips ?? 0} upcoming trips</span></div><div className="company-dashboard-action"><Link to="/manage-packages"><BriefcaseBusiness size={19}/> Create, edit or activate packages</Link><Link to="/packages"><CalendarDays size={19}/> Review your package catalogue</Link></div></section>
  </main>;
}
export default CompanyDashboard;
