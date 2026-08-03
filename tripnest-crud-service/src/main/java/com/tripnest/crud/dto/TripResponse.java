package com.tripnest.crud.dto;
import com.tripnest.crud.entity.TripStatus;
import java.time.LocalDate;
public record TripResponse(Integer tripId, Integer packageId, String packageName, LocalDate startDate,
        LocalDate endDate, Integer seatsAvailable, TripStatus status) {}
