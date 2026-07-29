import { useCallback, useEffect, useState } from "react";
import { useSelector } from "react-redux";
import { Link } from "react-router-dom";
import { Pencil, Plus, Trash2, X } from "lucide-react";
import packageService from "../services/packageService";
import "../css/Package.css";

const emptyPackage = {
  packageName: "",
  description: "",
  source: "",
  destination: "",
  price: "",
  status: "ACTIVE",
};

function PackageManager() {
  const { role } = useSelector((state) => state.auth);
  const [packages, setPackages] = useState([]);
  const [form, setForm] = useState(emptyPackage);
  const [editingId, setEditingId] = useState(null);
  const [error, setError] = useState("");
  const [message, setMessage] = useState("");
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [images, setImages] = useState([]);
  const [thumbnail] = useState(true);

  const loadPackages = useCallback(async () => {
    setLoading(true);
    try {
      setPackages(await packageService.list());
    } catch (apiError) {
      setError(apiError.response?.data?.message || "Unable to load packages.");
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    const initialLoad = window.setTimeout(() => {
      loadPackages();
    }, 0);

    return () => window.clearTimeout(initialLoad);
  }, [loadPackages]);

  const resetForm = () => {
    setForm(emptyPackage);
    setEditingId(null);
    setImages([]);
  };

  const handleChange = (event) => {
    setForm((current) => ({ ...current, [event.target.name]: event.target.value }));
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    setSaving(true);
    setError("");
    setMessage("");

    const payload = { ...form, price: Number(form.price) };
    try {
      if (editingId) {
        await packageService.update(editingId, payload);
        setMessage("Travel package updated successfully.");
      } else {
        const created = await packageService.create(payload);
        if (images.length) await packageService.uploadImages(created.packageId, images, thumbnail);
        setMessage("Travel package and images created successfully.");
      }
      resetForm();
      await loadPackages();
    } catch (apiError) {
      const fieldErrors = apiError.response?.data?.fieldErrors;
      setError((fieldErrors && Object.values(fieldErrors)[0]) || apiError.response?.data?.message || "Unable to save package.");
    } finally {
      setSaving(false);
    }
  };

  const startEditing = (travelPackage) => {
    setEditingId(travelPackage.packageId);
    setForm({
      packageName: travelPackage.packageName,
      description: travelPackage.description || "",
      source: travelPackage.source,
      destination: travelPackage.destination,
      price: travelPackage.price,
      status: travelPackage.status,
    });
    setMessage("");
    setError("");
  };

  const deletePackage = async (packageId) => {
    if (!window.confirm("Delete this travel package?")) return;
    setError("");
    try {
      await packageService.remove(packageId);
      setMessage("Travel package deleted.");
      await loadPackages();
    } catch (apiError) {
      setError(apiError.response?.data?.message || "Unable to delete package.");
    }
  };

  return (
    <main className="packages-page manager-page">
      <header className="packages-hero">
        <div><p>{role === "ADMIN" ? "Admin package control" : "Company workspace"}</p><h1>Manage travel packages</h1><span>Create, update, activate, deactivate, or remove travel packages.</span></div>
        <Link to="/packages" className="package-secondary-button">View packages</Link>
      </header>

      {error && <div className="form-alert error-alert">{error}</div>}
      {message && <div className="form-alert success-alert">{message}</div>}

      {(role === "COMPANY" || editingId) && (
        <form className="package-form-card" onSubmit={handleSubmit}>
          <div className="package-form-heading"><div><p>{editingId ? "Update package" : "New package"}</p><h2>{editingId ? "Edit travel package" : "Create a travel package"}</h2></div>{editingId && <button type="button" className="icon-text-button" onClick={resetForm}><X size={17} /> Cancel edit</button>}</div>
          <div className="form-grid">
            <label className="field-group"><span>Package name</span><input name="packageName" value={form.packageName} onChange={handleChange} required maxLength="150" /></label>
            <label className="field-group"><span>Price (₹)</span><input name="price" type="number" min="0.01" step="0.01" value={form.price} onChange={handleChange} required /></label>
            <label className="field-group"><span>Source city</span><input name="source" value={form.source} onChange={handleChange} required maxLength="100" /></label>
            <label className="field-group"><span>Destination city</span><input name="destination" value={form.destination} onChange={handleChange} required maxLength="100" /></label>
            {editingId && <label className="field-group"><span>Status</span><select name="status" value={form.status} onChange={handleChange}><option value="ACTIVE">ACTIVE</option><option value="INACTIVE">INACTIVE</option></select></label>}
            <label className="field-group full-width"><span>Description</span><textarea name="description" value={form.description} onChange={handleChange} rows="3" placeholder="Describe the travel experience" /></label>
            {!editingId && <label className="field-group full-width"><span>Package images</span><input type="file" accept="image/*" multiple onChange={(event) => setImages([...event.target.files])} /><small>Upload multiple images (maximum 5 MB each). The first image is the thumbnail.</small></label>}
          </div>
          <button className="package-primary-button" type="submit" disabled={saving}>{editingId ? <Pencil size={18} /> : <Plus size={18} />}{saving ? "Saving…" : editingId ? "Save changes" : "Create package"}</button>
        </form>
      )}

      {loading ? <div className="package-loading">Loading packages…</div> : (
        <section className="manage-package-list">
          {packages.map((travelPackage) => (
            <article className="manage-package-row" key={travelPackage.packageId}>
              <div><span>{travelPackage.status}</span><h2>{travelPackage.packageName}</h2><p>{travelPackage.source} to {travelPackage.destination} · ₹{Number(travelPackage.price).toLocaleString("en-IN")}</p></div>
              <div className="manage-actions"><button type="button" onClick={() => startEditing(travelPackage)}><Pencil size={17} /> Edit</button><button type="button" className="delete-package-button" onClick={() => deletePackage(travelPackage.packageId)}><Trash2 size={17} /> Delete</button></div>
            </article>
          ))}
          {!packages.length && <div className="package-empty"><Plus size={42} /><h2>No packages yet</h2><p>Create your first travel package above.</p></div>}
        </section>
      )}
    </main>
  );
}

export default PackageManager;
