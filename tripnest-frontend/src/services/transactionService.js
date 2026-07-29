import axios from "axios";

const transactionApi = axios.create({
  baseURL: import.meta.env.VITE_TRANSACTION_API_BASE_URL || "http://localhost:8080",
  headers: { "Content-Type": "application/json" },
});

transactionApi.interceptors.request.use((config) => {
  const token = localStorage.getItem("accessToken");
  if (token) config.headers.Authorization = `Bearer ${token}`;
  return config;
});

const unwrap = (request) => request.then((response) => response.data);

export const transactionService = {
  createBooking: (payload) => unwrap(transactionApi.post("/api/bookings", payload)),
  myBookings: () => unwrap(transactionApi.get("/api/bookings/me")),
  allBookings: () => unwrap(transactionApi.get("/api/bookings")),
  cancelBooking: (bookingId) => unwrap(transactionApi.patch(`/api/bookings/${bookingId}/cancel`)),
  updateBookingStatus: (bookingId, bookingStatus) => unwrap(transactionApi.patch(`/api/bookings/${bookingId}/status`, { bookingStatus })),
  receipt: (bookingId) => transactionApi.get(`/api/bookings/${bookingId}/receipt`, { responseType: "blob" }).then((response) => response.data),
  pay: (bookingId, paymentMethod) => unwrap(transactionApi.post(`/api/bookings/${bookingId}/payment`, { paymentMethod })),
  myPayments: () => unwrap(transactionApi.get("/api/payments/me")),
  allPayments: () => unwrap(transactionApi.get("/api/payments")),
  wishlist: () => unwrap(transactionApi.get("/api/wishlist")),
  addWishlist: (tripId) => unwrap(transactionApi.post("/api/wishlist", { tripId })),
  removeWishlist: (tripId) => unwrap(transactionApi.delete(`/api/wishlist/${tripId}`)),
  packageFeedback: (packageId) => unwrap(transactionApi.get(`/api/feedback/package/${packageId}`)),
  packageFeedbackSummary: (packageId) => unwrap(transactionApi.get(`/api/feedback/package/${packageId}/summary`)),
  createFeedback: (payload) => unwrap(transactionApi.post("/api/feedback", payload)),
  allFeedback: () => unwrap(transactionApi.get("/api/feedback")),
  deleteFeedback: (feedbackId) => unwrap(transactionApi.delete(`/api/feedback/${feedbackId}`)),
};

export default transactionService;
