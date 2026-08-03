package com.tripnest.crud.dto;

import com.tripnest.crud.entity.PackageStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record UpdatePackageRequest(
        @NotBlank @Size(max = 150) String packageName,
        String description,
        @NotBlank @Size(max = 100) String source,
        @NotBlank @Size(max = 100) String destination,
        @NotNull @DecimalMin(value = "0.01") BigDecimal price,
        @NotNull PackageStatus status
) {
}
