package com.tripnest.auth.dto;

import com.tripnest.auth.entity.CompanyStatus;

public record CompanyAdminResponse(
        Integer companyId,
        Integer userId,
        String companyName,
        String registrationNumber,
        String companyAddress,
        CompanyStatus status,
        String username,
        String email
) {
}
