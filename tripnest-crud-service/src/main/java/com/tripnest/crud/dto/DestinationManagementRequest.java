package com.tripnest.crud.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DestinationManagementRequest(
        @NotBlank(message = "City name is required") @Size(max = 100) String cityName,
        @NotBlank(message = "State name is required") @Size(max = 100) String stateName
) {
}
