import { createSlice } from "@reduxjs/toolkit";

const packageSlice = createSlice({
  name: "packages",
  initialState: {
    items: [],
    selectedPackage: null,
  },
  reducers: {
    setPackages: (state, action) => {
      state.items = action.payload;
    },
    setSelectedPackage: (state, action) => {
      state.selectedPackage = action.payload;
    },
  },
});

export const { setPackages, setSelectedPackage } = packageSlice.actions;
export default packageSlice.reducer;
