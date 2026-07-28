# Run TripNest locally

## One-time preparation

1. Create/import the database using `Database/TripNest_Database_Schema.sql`.
2. In every Java Run Configuration add these environment variables:

```text
TRIPNEST_DB_PASSWORD=root
TRIPNEST_JWT_SECRET=<your existing Base64 JWT secret>
```

`TRIPNEST_JWT_SECRET` must be the same for Auth, CRUD, and Transaction services.

## Start order

1. `TripnestDiscoveryServerApplication` — port `8761`
2. `TripnestAuthServiceApplication` — port `8081`
3. `TripnestCrudServiceApplication` — port `8082`
4. `TripnestTransactionServiceApplication` — port `8083`
5. `TripnestApiGatewayApplication` — port `8080`
6. React frontend: run `npm run dev` inside `tripnest-frontend`

## Local URLs

| Item | URL |
|---|---|
| React UI | `http://localhost:5173` |
| Eureka dashboard | `http://localhost:8761` |
| Gateway health | `http://localhost:8080/actuator/health` |
| Auth Swagger | `http://localhost:8081/swagger-ui/index.html` |
| CRUD Swagger | `http://localhost:8082/swagger-ui/index.html` |
| Transaction Swagger | `http://localhost:8083/swagger-ui/index.html` |

The frontend uses the gateway at `http://localhost:8080`. The gateway forwards each `/api/...` request to the correct service through Eureka.

## Quick demo flow

1. Open the frontend and register/log in as a tourist.
2. Browse Packages without login or search a destination.
3. Open a package, select an upcoming departure, and create a booking.
4. Open **My Bookings** and record a demo payment.
5. Check **Payment History**, Wishlist, and Feedback.
6. Open Eureka and show all running TripNest services registered there.

## Postman

Import the collections in `postman/`.

1. Use the Auth collection to get a Tourist login token.
2. Put that value into `touristToken` in the Transaction collection.
3. Run the Transaction requests in numeric order.
