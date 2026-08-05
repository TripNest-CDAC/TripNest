import { useEffect, useRef, useState } from "react";
import { LoaderCircle, MapPin, Search } from "lucide-react";
import packageService from "../services/packageService";

function DestinationSearch({ onSearch, initialDestinationId = "", initialLabel = "", className = "" }) {
  const root = useRef(null);
  const [value, setValue] = useState(initialLabel);
  const [selected, setSelected] = useState(initialDestinationId ? { id: Number(initialDestinationId), displayName: initialLabel } : null);
  const [suggestions, setSuggestions] = useState([]); const [open, setOpen] = useState(false); const [loading, setLoading] = useState(false); const [error, setError] = useState(""); const [activeIndex, setActiveIndex] = useState(-1);

  useEffect(() => {
    const close = (event) => { if (root.current && !root.current.contains(event.target)) setOpen(false); };
    document.addEventListener("mousedown", close); return () => document.removeEventListener("mousedown", close);
  }, []);
  useEffect(() => {
    if (!open) return undefined;
    const controller = new AbortController();
    const timer = window.setTimeout(async () => {
      setLoading(true); setError("");
      try { setSuggestions(await packageService.destinations(value, controller.signal)); setActiveIndex(-1); }
      catch (apiError) { if (apiError.name !== "CanceledError") { setSuggestions([]); setError("Unable to load destinations. Please try again."); } }
      finally { if (!controller.signal.aborted) setLoading(false); }
    }, 300);
    return () => { window.clearTimeout(timer); controller.abort(); };
  }, [value, open]);

  const select = (destination) => { setSelected(destination); setValue(destination.displayName); setOpen(false); setError(""); };
  const change = (event) => { setValue(event.target.value); setSelected(null); setOpen(true); setError(""); };
  const submit = (event) => { event.preventDefault(); if (!selected) { setError("Please select a destination from the available suggestions."); setOpen(true); return; } onSearch(selected); };
  const keyboard = (event) => {
    if (event.key === "Escape") { setOpen(false); return; }
    if (event.key === "ArrowDown") { event.preventDefault(); setOpen(true); setActiveIndex((index) => Math.min(index + 1, suggestions.length - 1)); }
    if (event.key === "ArrowUp") { event.preventDefault(); setActiveIndex((index) => Math.max(index - 1, 0)); }
    if (event.key === "Enter" && open && activeIndex >= 0) { event.preventDefault(); select(suggestions[activeIndex]); }
  };

  return <form ref={root} className={`destination-search ${className}`} onSubmit={submit}>
    <MapPin size={20} aria-hidden="true" />
    <div className="destination-search-control">
      <input value={value} onChange={change} onFocus={() => setOpen(true)} onKeyDown={keyboard} placeholder="Where do you want to go?" aria-label="Destination" aria-expanded={open} aria-autocomplete="list" />
      {open && <div className="destination-dropdown" role="listbox">
        {loading && <p className="destination-message"><LoaderCircle size={16} className="spin" /> Finding destinations…</p>}
        {!loading && error && <p className="destination-message error">{error}</p>}
        {!loading && !error && suggestions.length === 0 && <p className="destination-message">No destinations found<br /><small>Please select a destination from the available suggestions.</small></p>}
        {!loading && !error && suggestions.map((destination, index) => <button type="button" role="option" aria-selected={selected?.id === destination.id} className={index === activeIndex ? "highlighted" : ""} key={destination.id} onMouseDown={(event) => event.preventDefault()} onClick={() => select(destination)}><MapPin size={17} /><span>{destination.displayName}</span></button>)}
      </div>}
      {error && !open && <small className="destination-inline-error">{error}</small>}
    </div>
    <button type="submit" disabled={!selected}><Search size={18} /> Search</button>
  </form>;
}

export default DestinationSearch;
