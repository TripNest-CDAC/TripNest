# TripNest — Final Project Requirements

## 1. Project boundary

TripNest is a Java Spring Boot microservices travel-booking platform with a React frontend and MySQL database.

- Java 21, Spring Boot, Maven
- React + Vite frontend
- MySQL 8 database
- No AI module
- No .NET module

## 2. Services

| Service | Port | Responsibility | Status |
|---|---:|---|---|
| `tripnest-auth-service` | 8081 | Registration, login, JWT, profile, company approval, platform-user administration | Implemented — final verification pending |
| `tripnest-crud-service` | 8082 | Packages, package images, trip planning, package/trip administration | Implemented — final verification pending |
| `tripnest-transaction-service` | 8083 | Booking, travellers, payment records, wishlist, feedback | To build |
| `tripnest-discovery-server` | 8761 | Eureka service registration and lookup | To build |
| `tripnest-api-gateway` | 8080 | A single public API entry point and route forwarding | To build |

## 3. Roles and access

### Tourist

- Browse active packages and available upcoming trips without logging in.
- Register and log in.
- Manage own profile and profile image.
- Add or remove an available trip from wishlist.
- Book an available upcoming trip for one or more travellers.
- View own bookings and cancel an eligible booking.
- Record a demo payment against own pending booking.
- View own payment history.
- Submit one rating and comment for a completed booking.

### Company

- Register a company account; access is allowed after admin approval.
- Manage own profile and profile image.
- Create, edit, activate/deactivate, and upload images for only its own packages.
- View package counts and booking amount in dashboard.

### Admin

- View, activate/deactivate, or delete users and companies.
- Approve or suspend company accounts.
- View, activate/deactivate, or delete any package.
- Create, edit, cancel, or delete trips.
- Trip dates must not be in the past and end date must not precede start date.
- View all bookings, payments, and feedback.
- Update booking status when required and delete inappropriate feedback.

## 4. Transaction service functional requirements

### Booking

1. Only an authenticated active TOURIST can create a booking.
2. The trip must be UPCOMING, belong to an ACTIVE package, have a future start date, and have enough available seats.
3. Number of travellers must be between 1 and available seats.
4. Booking stores total amount from the package price multiplied by traveller count; the client cannot choose the price.
5. Booking creation decreases trip seats atomically.
6. A duplicate active booking for the same tourist and trip is rejected.
7. Cancelling an eligible booking restores its seats and cannot be repeated.

### Payment

1. Payment belongs to one booking and only the booking owner can create it.
2. The payment amount must equal the booking amount.
3. Only supported methods are UPI, CARD, NET_BANKING, and CASH.
4. This academic project records a demo payment; it must not collect real card, bank, or UPI credentials.
5. Payment generates a unique reference and exposes status in payment history.

### Wishlist

1. Only an authenticated tourist can add/remove their own wishlist entries.
2. A duplicate wishlist entry is rejected.
3. The tourist can view only their wishlist.

### Feedback

1. Only the booking owner can submit feedback.
2. Feedback is allowed only after a COMPLETED trip/booking.
3. Rating is 1–5 and comment length is limited.
4. A tourist can submit one feedback item per booking.

## 5. Security and validation requirements

- Every protected API validates the JWT issued by Service 1.
- Each service uses the same configured JWT secret for local development.
- Authorization checks use the JWT `userId` and `role` claims; client-provided user IDs are never trusted.
- Validation errors return understandable HTTP 400 responses.
- Unauthorized calls return 401; insufficient role returns 403; missing records return 404.
- Passwords remain stored only as BCrypt hashes in Service 1.
- Uploaded image files have type/size validation and are not stored in Git.

## 6. Frontend requirements

- Public package/trip browsing and destination search remain usable without login.
- Booking flow shows trip, traveller count, calculated amount, and validation messages.
- My Bookings shows booking, traveller, payment, and cancellation status.
- Wishlist, payment history, and feedback pages call the real transaction APIs.
- Dashboards show data loaded from APIs and update immediately after actions.
- UI must not expose internal security implementation details.

## 7. Infrastructure and final acceptance

1. All services register in Eureka.
2. Gateway routes auth, CRUD, and transaction requests through port 8080.
3. Swagger is available for each backend service during development.
4. Postman collections cover each service’s main happy path and validation failures.
5. Maven tests and frontend lint/build pass.
6. A clean machine can set database password/JWT environment variables, import the schema, and run the project with documented steps.

## Implementation order

1. Build and test transaction service backend.
2. Connect and complete transaction frontend flows.
3. Add discovery server and gateway.
4. Run end-to-end API and UI verification, update Postman/readme, and prepare presentation notes.
