import { useCallback, useEffect, useState } from "react";
import { useDispatch } from "react-redux";
import { useNavigate } from "react-router-dom";
import {
  BadgeCheck,
  Building2,
  Mail,
  MapPin,
  Pencil,
  Phone,
  Save,
  ShieldCheck,
  UserRound,
  X,
} from "lucide-react";
import { logout } from "../redux/authSlice";
import authService from "../services/authService";
import "../css/Dashboard.css";

const emptyForm = {
  firstName: "",
  lastName: "",
  phone: "",
  address: "",
  companyName: "",
  companyAddress: "",
};

function DashboardProfile({ title, description }) {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const [profile, setProfile] = useState(null);
  const [form, setForm] = useState(emptyForm);
  const [editing, setEditing] = useState(false);
  const [saving, setSaving] = useState(false);
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");

  const loadProfile = useCallback(() => {
    authService
      .currentUser()
      .then((response) => {
        setProfile(response);
        setForm({
          firstName: response.firstName || "",
          lastName: response.lastName || "",
          phone: response.phone || "",
          address: response.address || "",
          companyName: response.companyName || "",
          companyAddress: response.companyAddress || "",
        });
      })
      .catch((apiError) => {
        setError(
          apiError.response?.data?.message || "Unable to load your profile.",
        );

        if (apiError.response?.status === 401) {
          dispatch(logout());
          navigate("/login", { replace: true });
        }
      });
  }, [dispatch, navigate]);

  useEffect(() => {
    loadProfile();
  }, [loadProfile]);

  const handleChange = (event) => {
    setForm((current) => ({
      ...current,
      [event.target.name]: event.target.value,
    }));
  };

  const cancelEditing = () => {
    setForm({
      firstName: profile.firstName || "",
      lastName: profile.lastName || "",
      phone: profile.phone || "",
      address: profile.address || "",
      companyName: profile.companyName || "",
      companyAddress: profile.companyAddress || "",
    });
    setEditing(false);
    setError("");
  };

  const saveProfile = async (event) => {
    event.preventDefault();
    setError("");
    setMessage("");
    setSaving(true);

    try {
      const updatedProfile = await authService.updateProfile(form);
      setProfile(updatedProfile);
      setForm({
        firstName: updatedProfile.firstName || "",
        lastName: updatedProfile.lastName || "",
        phone: updatedProfile.phone || "",
        address: updatedProfile.address || "",
        companyName: updatedProfile.companyName || "",
        companyAddress: updatedProfile.companyAddress || "",
      });
      setEditing(false);
      setMessage("Your profile has been updated.");
    } catch (apiError) {
      const fieldErrors = apiError.response?.data?.fieldErrors;
      setError(
        (fieldErrors && Object.values(fieldErrors)[0]) ||
          apiError.response?.data?.message ||
          "Unable to update your profile.",
      );
    } finally {
      setSaving(false);
    }
  };

  const uploadImage = async (event) => { const file = event.target.files?.[0]; if (!file) return; try { setProfile(await authService.uploadProfileImage(file)); setMessage("Profile photo updated."); } catch { setError("Unable to upload profile photo."); } };
  const removeImage = async () => { try { setProfile(await authService.removeProfileImage()); setMessage("Profile photo removed."); } catch { setError("Unable to remove profile photo."); } };

  return (
    <section className="dashboard-profile-section">
      <header className="dashboard-header">
        <div>
          <p>Your TripNest space</p>
          <h1>{title}</h1>
          <span>{description}</span>
        </div>
        <div className="dashboard-badge"><ShieldCheck size={21} /> Your account</div>
      </header>

      {error && <div className="form-alert error-alert">{error}</div>}
      {message && <div className="form-alert success-alert">{message}</div>}

      {!profile && !error && <div className="dashboard-loading">Loading your profile…</div>}

      {profile && (
        <section className="profile-layout">
          <article className="profile-card">
            <div className="profile-avatar">
              {profile.profileImagePath ? <img src={`http://localhost:8081${profile.profileImagePath}`} alt="Profile" /> : profile.role === "COMPANY" ? <Building2 size={38} /> : <UserRound size={38} />}
            </div>
            <label className="edit-profile-button"><input type="file" accept="image/*" hidden onChange={uploadImage} />Add photo</label>
            {profile.profileImagePath && <button type="button" className="cancel-profile-button" onClick={removeImage}>Remove photo</button>}
            <p>{profile.role} ACCOUNT</p>
            <h2>{profile.firstName} {profile.lastName || ""}</h2>
            <span>@{profile.username}</span>
            <div className="status-pill">
              <BadgeCheck size={17} />
              {profile.companyStatus || profile.userStatus}
            </div>
          </article>

          <article className="profile-details">
            <div className="detail-heading">
              <div>
                <p>My profile</p>
                <h2>{editing ? "Edit your details" : "Your account details"}</h2>
              </div>
              {!editing && (
                <button className="edit-profile-button" type="button" onClick={() => setEditing(true)}>
                  <Pencil size={17} /> Edit profile
                </button>
              )}
            </div>

            {editing ? (
              <form className="profile-form" onSubmit={saveProfile}>
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
                  {profile.role === "COMPANY" && (
                    <>
                      <label className="field-group">
                        <span>Company name</span>
                        <input name="companyName" value={form.companyName} onChange={handleChange} required maxLength="150" />
                      </label>
                      <label className="field-group">
                        <span>Company address</span>
                        <input name="companyAddress" value={form.companyAddress} onChange={handleChange} />
                      </label>
                    </>
                  )}
                </div>
                <div className="profile-form-actions">
                  <button className="save-profile-button" type="submit" disabled={saving}>
                    <Save size={17} /> {saving ? "Saving…" : "Save changes"}
                  </button>
                  <button className="cancel-profile-button" type="button" onClick={cancelEditing} disabled={saving}>
                    <X size={17} /> Cancel
                  </button>
                </div>
              </form>
            ) : (
              <>
                <div className="detail-grid">
                  <div><Mail size={20} /><span>Email</span><strong>{profile.email}</strong></div>
                  <div><Phone size={20} /><span>Phone</span><strong>{profile.phone || "Not provided"}</strong></div>
                  <div><MapPin size={20} /><span>Address</span><strong>{profile.address || "Not provided"}</strong></div>
                  <div><UserRound size={20} /><span>Account type</span><strong>{profile.role}</strong></div>
                </div>
                {profile.role === "COMPANY" && (
                  <div className="company-profile-details">
                    <p>Company details</p>
                    <div><span>Company name</span><strong>{profile.companyName}</strong></div>
                    <div><span>Registration number</span><strong>{profile.registrationNumber}</strong></div>
                    <div><span>Company address</span><strong>{profile.companyAddress || "Not provided"}</strong></div>
                  </div>
                )}
              </>
            )}
          </article>
        </section>
      )}
    </section>
  );
}

export default DashboardProfile;
