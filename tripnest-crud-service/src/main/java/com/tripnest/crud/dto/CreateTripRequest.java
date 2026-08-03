package com.tripnest.crud.dto;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
public record CreateTripRequest(@NotNull Integer packageId, @NotNull @FutureOrPresent LocalDate startDate,
        @NotNull LocalDate endDate, @NotNull @Min(0) Integer seatsAvailable) {}
