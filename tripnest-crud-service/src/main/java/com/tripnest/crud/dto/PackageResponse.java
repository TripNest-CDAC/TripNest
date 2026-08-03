package com.tripnest.crud.dto;

import com.tripnest.crud.entity.PackageStatus;

import java.math.BigDecimal;
import java.util.List;

public record PackageResponse(
        Integer packageId,
        Integer companyId,
        String companyName,
        String packageName,
        String description,
        String source,
        String destination,
        BigDecimal price,
        PackageStatus status,
        String thumbnailUrl,
        List<String> imageUrls,
        boolean available
) {
}
