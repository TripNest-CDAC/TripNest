import { Routes, Route } from "react-router-dom";

import Home from "./pages/Home.jsx";
import Login from "./pages/Login.jsx";
import Register from "./pages/Register.jsx";
import AdminDashboard from "./pages/AdminDashboard.jsx";
import UserDashboard from "./pages/UserDashboard.jsx";
import CompanyDashboard from "./pages/CompanyDashboard.jsx";
import PackageList from "./pages/PackageList.jsx";
import PackageDetails from "./pages/PackageDetails.jsx";
import PackageManager from "./pages/PackageManager.jsx";
import Navbar from "./components/Navbar";
import Footer from "./components/Footer.jsx";
import ProtectedRoutes from "./components/ProtectedRoutes.jsx";

function App() {
  return (
    <div className="app-shell">
      <Navbar />
      <div className="app-content">
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />
          <Route path="/destinations" element={<PackageList />} />
          <Route path="/packages" element={<PackageList />} />
          <Route path="/packages/:id" element={<PackageDetails />} />
          <Route
            path="/manage-packages"
            element={
              <ProtectedRoutes allowedRoles={["COMPANY", "ADMIN"]}>
                <PackageManager />
              </ProtectedRoutes>
            }
          />

          <Route
            path="/user-dashboard"
            element={
              <ProtectedRoutes allowedRoles={["TOURIST"]}>
                <UserDashboard />
              </ProtectedRoutes>
            }
          />

          <Route
            path="/admin-dashboard"
            element={
              <ProtectedRoutes allowedRoles={["ADMIN"]}>
                <AdminDashboard />
              </ProtectedRoutes>
            }
          />

          <Route
            path="/company-dashboard"
            element={
              <ProtectedRoutes allowedRoles={["COMPANY"]}>
                <CompanyDashboard />
              </ProtectedRoutes>
            }
          />

          <Route path="*" element={<Home />} />
        </Routes>
      </div>
      <Footer />
    </div>
  );
}

export default App;
