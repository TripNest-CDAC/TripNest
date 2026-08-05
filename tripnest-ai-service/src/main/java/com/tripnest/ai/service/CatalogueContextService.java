package com.tripnest.ai.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class CatalogueContextService {
    private final JdbcTemplate jdbcTemplate;

    public CatalogueContextService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public String activeCatalogue() {
        List<CatalogueTrip> trips = jdbcTemplate.query("""
                SELECT p.package_name, p.source, p.destination, p.price,
                       t.start_date, t.end_date, t.seats_available
                FROM travel_package p
                JOIN trips t ON t.package_id = p.package_id
                WHERE p.status = 'ACTIVE'
                  AND t.trip_status = 'UPCOMING'
                  AND t.start_date > CURDATE()
                  AND t.seats_available > 0
                ORDER BY t.start_date ASC
                LIMIT 30
                """, (resultSet, rowNumber) -> new CatalogueTrip(
                resultSet.getString("package_name"),
                resultSet.getString("source"),
                resultSet.getString("destination"),
                resultSet.getBigDecimal("price"),
                resultSet.getObject("start_date", LocalDate.class),
                resultSet.getObject("end_date", LocalDate.class),
                resultSet.getInt("seats_available")
        ));
        if (trips.isEmpty()) return "No active TripNest packages or future trips are currently available.";
        return trips.stream().map(CatalogueTrip::toContextLine).reduce((first, second) -> first + "\n" + second).orElse("");
    }

    private record CatalogueTrip(String packageName, String source, String destination, BigDecimal price,
                                 LocalDate startDate, LocalDate endDate, int seatsAvailable) {
        private String toContextLine() {
            return "Package: " + packageName + " | Route: " + source + " to " + destination
                    + " | Price per person: ₹" + price + " | Dates: " + startDate + " to " + endDate
                    + " | Seats available: " + seatsAvailable;
        }
    }
}
