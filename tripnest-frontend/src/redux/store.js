import { configureStore } from "@reduxjs/toolkit";
import authReducer from "./authSlice";
import packageReducer from "./packageSlice";
import bookingReducer from "./bookingSlice";

const store = configureStore({
  reducer: {
    auth: authReducer,
    packages: packageReducer,
    bookings: bookingReducer,
  },
});

export default store;
