import api from "./api";

export const authService = {
  async register(payload) {
    const response = await api.post("/api/auth/register", payload);
    return response.data;
  },

  async login(username, password) {
    const response = await api.post("/api/auth/login", {
      username,
      password,
    });
    return response.data;
  },

  async currentUser() {
    const response = await api.get("/api/users/me");
    return response.data;
  },

  async updateProfile(payload) {
    const response = await api.put("/api/users/me", payload);
    return response.data;
  },
  async uploadProfileImage(image) {
    const data = new FormData(); data.append("image", image);
    return (await api.post("/api/users/me/profile-image", data, { headers: { "Content-Type": "multipart/form-data" } })).data;
  },
  async removeProfileImage() { return (await api.delete("/api/users/me/profile-image")).data; },
  async users() { return (await api.get("/api/admin/users")).data; },
  async updateUserStatus(userId, status) { return (await api.patch(`/api/admin/users/${userId}/status`, { status })).data; },
  async deleteUser(userId) { await api.delete(`/api/admin/users/${userId}`); },
  async companies() { return (await api.get("/api/admin/companies")).data; },

  async pendingCompanies() {
    const response = await api.get("/api/admin/companies/pending");
    return response.data;
  },

  async approveCompany(companyId) {
    const response = await api.patch(
      `/api/admin/companies/${companyId}/approve`,
    );
    return response.data;
  },

  async suspendCompany(companyId) {
    const response = await api.patch(
      `/api/admin/companies/${companyId}/suspend`,
    );
    return response.data;
  },

  async health() {
    const response = await api.get("/actuator/health");
    return response.data;
  },
};

export default authService;
