import { useState } from "react";
import { Link } from "react-router-dom";
import {
  ArrowRight,
  Building2,
  CheckCircle2,
  MapPin,
  UserRound,
} from "lucide-react";
import authService from "../services/authService";
import "../css/Register.css";

const initialForm = {
  username: "",
  password: "",
  firstName: "",
  lastName: "",
  email: "",
  phone: "",
  address: "",
  companyName: "",
  registrationNumber: "",
  companyAddress: "",
};

function Register() {
  const [role, setRole] = useState("TOURIST");
  const [form, setForm] = useState(initialForm);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState(null);
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
      const payload = {
        username: form.username.trim(),
        password: form.password,
        firstName: form.firstName.trim(),
        lastName: form.lastName.trim() || null,
        email: form.email.trim(),
        phone: form.phone.trim() || null,
        address: form.address.trim() || null,
        role,
        companyName: role === "COMPANY" ? form.companyName.trim() : null,
        registrationNumber:
          role === "COMPANY" ? form.registrationNumber.trim() : null,
        companyAddress:
          role === "COMPANY" ? form.companyAddress.trim() || null : null,
      };

      const response = await authService.register(payload);
      setSuccess(response);
      setForm(initialForm);
    } catch (apiError) {
      const fieldErrors = apiError.response?.data?.fieldErrors;
      const firstFieldError = fieldErrors
        ? Object.values(fieldErrors)[0]
        : null;

      setError(
        firstFieldError ||
          apiError.response?.data?.message ||
          "Registration failed. Please review your information.",
      );
    } finally {
      setLoading(false);
    }
  };

  if (success) {
    return (
      <main className="registration-result">
        <div className="result-card">
          <CheckCircle2 size={54} />
          <p>Registration complete</p>
          <h1>Welcome, {success.username}.</h1>
          <span>{success.message}</span>
          {success.companyStatus === "PENDING" && (
            <div className="pending-note">
              Your company is pending administrator approval. Login will be
              enabled after approval.
            </div>
          )}
          <div className="result-actions">
            <Link to="/login" className="submit-button">
              Continue to login <ArrowRight size={19} />
            </Link>
            <button type="button" onClick={() => setSuccess(null)}>
              Register another account
            </button>
          </div>
        </div>
      </main>
    );
  }

  return (
    <main className="register-page">
      <section className="register-intro">
        <p className="auth-kicker">Create your TripNest account</p>
        <h1>Choose how you want to explore.</h1>
        <span>
          Tourist accounts activate immediately. Company accounts require
          administrator approval before login.
        </span>

        <div className="role-selector" aria-label="Account type">
          <button
            type="button"
            className={role === "TOURIST" ? "active" : ""}
            onClick={() => setRole("TOURIST")}
          >
            <UserRound size={24} />
            <span><strong>Tourist</strong>Plan and manage journeys</span>
          </button>
          <button
            type="button"
            className={role === "COMPANY" ? "active" : ""}
            onClick={() => setRole("COMPANY")}
          >
            <Building2 size={24} />
            <span><strong>Travel Company</strong>Offer trusted experiences</span>
          </button>
        </div>
      </section>

      <form className="register-form" onSubmit={handleSubmit}>
        {error && <div className="form-alert error-alert">{error}</div>}

        <div className="form-section-heading">
          <span>01</span>
          <div><h2>Account details</h2><p>Credentials used for secure login.</p></div>
        </div>

        <div className="form-grid">
          <label className="field-group">
            <span>Username</span>
            <input name="username" value={form.username} onChange={handleChange} required maxLength="100" />
          </label>
          <label className="field-group">
            <span>Email address</span>
            <input type="email" name="email" value={form.email} onChange={handleChange} required maxLength="150" />
          </label>
          <label className="field-group full-width">
            <span>Password</span>
            <input type="password" name="password" value={form.password} onChange={handleChange} required minLength="8" maxLength="72" />
            <small>Use 8–72 characters.</small>
          </label>
        </div>

        <div className="form-section-heading">
          <span>02</span>
          <div><h2>Personal details</h2><p>Tell us who owns this account.</p></div>
        </div>

        <div className="form-grid">
          <label className="field-group">
            <span>First name</span>
            <input name="firstName" value={form.firstName} onChange={handleChange} required maxLength="100" />
          </label>
          <label className="field-group">
            <span>Last name</span>
            <input name="lastName" value={form.lastName} onChange={handleChange} maxLength="100" />
          </label>
          <label className="field-group">
            <span>Phone</span>
            <input name="phone" value={form.phone} onChange={handleChange} maxLength="15" />
          </label>
          <label className="field-group">
            <span>Address</span>
            <input name="address" value={form.address} onChange={handleChange} />
          </label>
        </div>

        {role === "COMPANY" && (
          <>
            <div className="form-section-heading">
              <span>03</span>
              <div><h2>Company details</h2><p>Required for administrator review.</p></div>
            </div>
            <div className="form-grid">
              <label className="field-group">
                <span>Company name</span>
                <input name="companyName" value={form.companyName} onChange={handleChange} required maxLength="150" />
              </label>
              <label className="field-group">
                <span>Registration number</span>
                <input name="registrationNumber" value={form.registrationNumber} onChange={handleChange} required maxLength="100" />
              </label>
              <label className="field-group full-width">
                <span>Company address</span>
                <div className="field-control simple-control">
                  <MapPin size={18} />
                  <input name="companyAddress" value={form.companyAddress} onChange={handleChange} />
                </div>
              </label>
            </div>
          </>
        )}

        <button className="submit-button register-submit" type="submit" disabled={loading}>
          {loading ? "Creating account..." : `Create ${role === "COMPANY" ? "company" : "tourist"} account`}
          {!loading && <ArrowRight size={19} />}
        </button>
        <p className="form-switch">
          Already registered? <Link to="/login">Sign in</Link>
        </p>
      </form>
    </main>
  );
}

export default Register;
