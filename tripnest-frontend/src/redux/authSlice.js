import { createSlice } from "@reduxjs/toolkit";

const initialState = {
  user: localStorage.getItem("username")
    ? {
        username: localStorage.getItem("username"),
        role: localStorage.getItem("role"),
      }
    : null,
  token: localStorage.getItem("accessToken") || null,
  role: localStorage.getItem("role") || null,
  isAuthenticated: Boolean(localStorage.getItem("accessToken")),
};

const authSlice = createSlice({
  name: "auth",
  initialState,
  reducers: {
    loginSuccess: (state, action) => {
      state.user = {
        userId: action.payload.userId,
        username: action.payload.username,
        role: action.payload.role,
      };
      state.token = action.payload.accessToken;
      state.role = action.payload.role;
      state.isAuthenticated = true;
      localStorage.setItem("accessToken", action.payload.accessToken);
      localStorage.setItem("role", action.payload.role);
      localStorage.setItem("username", action.payload.username);
    },
    logout: (state) => {
      state.user = null;
      state.token = null;
      state.role = null;
      state.isAuthenticated = false;
      localStorage.removeItem("accessToken");
      localStorage.removeItem("role");
      localStorage.removeItem("username");
    },
  },
});

export const { loginSuccess, logout } = authSlice.actions;
export default authSlice.reducer;
