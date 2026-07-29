import axios from "axios";

const crudApi = axios.create({
  baseURL: import.meta.env.VITE_CRUD_API_BASE_URL || "http://localhost:8080",
  headers: { "Content-Type": "application/json" },
});

crudApi.interceptors.request.use((config) => {
  const token = localStorage.getItem("accessToken");
  if (token && !config.skipAuth) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export const packageService = {
  async list(destination = "") {
    const response = await crudApi.get("/api/packages", {
      params: destination ? { destination } : {},
    });
    return response.data;
  },
  async available(destination = "", destinationId = "") {
    const response = await crudApi.get("/api/packages/available", { params: destinationId ? { destinationId } : destination ? { destination } : {}, skipAuth: true });
    return response.data;
  },
  async destinations(query = "", signal) { return (await crudApi.get("/api/destinations/search", { params: query ? { query } : {}, signal, skipAuth: true })).data; },
  async adminDestinations() { return (await crudApi.get("/api/admin/destinations")).data; },
  async createDestination(payload) { return (await crudApi.post("/api/admin/destinations", payload)).data; },
  async updateDestination(destinationId, payload) { return (await crudApi.put(`/api/admin/destinations/${destinationId}`, payload)).data; },
  async updateDestinationStatus(destinationId, active) { return (await crudApi.patch(`/api/admin/destinations/${destinationId}/status`, { active })).data; },
  async removeDestination(destinationId) { await crudApi.delete(`/api/admin/destinations/${destinationId}`); },

  async get(packageId) {
    const response = await crudApi.get(`/api/packages/${packageId}`);
    return response.data;
  },

  async create(payload) {
    const response = await crudApi.post("/api/packages", payload);
    return response.data;
  },

  async update(packageId, payload) {
    const response = await crudApi.put(`/api/packages/${packageId}`, payload);
    return response.data;
  },

  async remove(packageId) {
    await crudApi.delete(`/api/packages/${packageId}`);
  },
  async uploadImages(packageId, files, thumbnail = false) {
    const formData = new FormData();
    [...files].forEach((file) => formData.append("images", file));
    const response = await crudApi.post(`/api/packages/${packageId}/images?thumbnail=${thumbnail}`, formData, { headers: { "Content-Type": "multipart/form-data" } });
    return response.data;
  },
  async companyDashboard() { return (await crudApi.get("/api/dashboard/company")).data; },
  async adminDashboard() { return (await crudApi.get("/api/dashboard/admin")).data; },
  async trips(packageId) { return (await crudApi.get("/api/trips", { params: packageId ? { packageId } : {} })).data; },
  async availableTrips() { return (await crudApi.get("/api/trips/available", { skipAuth: true })).data; },
  async createTrip(payload) { return (await crudApi.post("/api/trips", payload)).data; },
  async updateTrip(tripId, payload) { return (await crudApi.put(`/api/trips/${tripId}`, payload)).data; },
  async removeTrip(tripId) { await crudApi.delete(`/api/trips/${tripId}`); },
};

export default packageService;
