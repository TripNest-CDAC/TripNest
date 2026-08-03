package com.tripnest.crud.dto;

public record AdminDestinationResponse(
        Integer destinationId,
        String cityName,
        String stateName,
        boolean active
) {
}
