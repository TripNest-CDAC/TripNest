import { useState } from "react";
import { useDispatch } from "react-redux";
import { Link, useNavigate } from "react-router-dom";
import { ArrowRight, Eye, EyeOff, KeyRound, Mail, ShieldCheck } from "lucide-react";
import { loginSuccess } from "../redux/authSlice";
import authService from "../services/authService";
import "../css/Login.css";

function Login() {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const [form, setForm] = useState({ username: "", password: "" });
  const [showPassword, setShowPassword] = useState(false);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const handleChange = (event) => {
    setForm((current) => ({
      ...current,
      [event.target.name]: event.target.value,
    }));
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    setError("");
    setLoading(true);

    try {
      const response = await authService.login(
        form.username.trim(),
        form.password,
      );
      dispatch(loginSuccess(response));

      const destination =
        response.role === "ADMIN"
          ? "/admin-dashboard"
          : response.role === "COMPANY"
            ? "/company-dashboard"
            : "/user-dashboard";

      navigate(destination, { replace: true });
    } catch (apiError) {
      setError(
        apiError.response?.data?.message ||
          "Unable to sign in. Please check your credentials.",
      );
    } finally {
      setLoading(false);
    }
  };

  return (
    <main className="auth-page">
      <section className="auth-side-panel">
        <div className="auth-side-content">
          <div className="auth-side-icon"><ShieldCheck size={34} /></div>
          <p className="auth-kicker">Welcome to TripNest</p>
          <h1>Welcome back to your TripNest journey.</h1>
          <p>
            Sign in to continue planning, saving, and enjoying your journeys.
          </p>
          <div className="auth-points">
            <span><KeyRound size={18} /> Your travel plans in one place</span>
            <span><ShieldCheck size={18} /> Made for every kind of traveller</span>
          </div>
        </div>
      </section>

      <section className="auth-form-panel">
        <form className="auth-form-card" onSubmit={handleSubmit}>
          <div className="form-heading">
            <p>Member login</p>
            <h2>Sign in to TripNest</h2>
            <span>Enter your username/email and password.</span>
          </div>

          {error && <div className="form-alert error-alert">{error}</div>}

          <label className="field-group">
            <span>Username or email</span>
            <div className="field-control">
              <Mail size={19} />
              <input
                type="text"
                name="username"
                value={form.username}
                onChange={handleChange}
                placeholder="admin@tripnest.demo"
                autoComplete="username"
                required
              />
            </div>
          </label>

          <label className="field-group">
            <span>Password</span>
            <div className="field-control">
              <KeyRound size={19} />
              <input
                type={showPassword ? "text" : "password"}
                name="password"
                value={form.password}
                onChange={handleChange}
                placeholder="Enter your password"
                autoComplete="current-password"
                required
              />
              <button
                className="password-toggle"
                type="button"
                onClick={() => setShowPassword((visible) => !visible)}
                aria-label={showPassword ? "Hide password" : "Show password"}
              >
                {showPassword ? <EyeOff size={19} /> : <Eye size={19} />}
              </button>
            </div>
          </label>

          <button className="submit-button" type="submit" disabled={loading}>
            {loading ? "Signing in..." : "Sign in"}
            {!loading && <ArrowRight size={19} />}
          </button>

          <p className="form-switch">
            New to TripNest? <Link to="/register">Create an account</Link>
          </p>
        </form>
      </section>
    </main>
  );
}

export default Login;
