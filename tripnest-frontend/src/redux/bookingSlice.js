import { createSlice } from "@reduxjs/toolkit";

const bookingSlice = createSlice({
  name: "bookings",
  initialState: {
    items: [],
  },
  reducers: {
    setBookings: (state, action) => {
      state.items = action.payload;
    },
  },
});

export const { setBookings } = bookingSlice.actions;
export default bookingSlice.reducer;
