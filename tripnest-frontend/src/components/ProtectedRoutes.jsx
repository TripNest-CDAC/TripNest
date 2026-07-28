import { Navigate } from "react-router-dom";

function ProtectedRoutes({ children, allowedRoles }) {
  const token = localStorage.getItem("accessToken");
  const role = localStorage.getItem("role");

  if (!token) {
    return <Navigate to="/login" replace />;
  }

  if (allowedRoles && !allowedRoles.includes(role)) {
    const destination =
      role === "ADMIN"
        ? "/admin-dashboard"
        : role === "COMPANY"
          ? "/company-dashboard"
          : "/user-dashboard";

    return <Navigate to={destination} replace />;
  }

  return children;
}

export default ProtectedRoutes;
