package com.tripnest.crud.dto;
import com.tripnest.crud.entity.TripStatus;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
public record UpdateTripRequest(@NotNull LocalDate startDate,@NotNull LocalDate endDate,@NotNull @Min(0) Integer seatsAvailable,@NotNull TripStatus status) {}
