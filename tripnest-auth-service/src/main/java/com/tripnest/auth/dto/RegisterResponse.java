package com.tripnest.auth.dto;

import com.tripnest.auth.entity.CompanyStatus;
import com.tripnest.auth.entity.RoleName;
import com.tripnest.auth.entity.UserStatus;

public record RegisterResponse(
        Integer userId,
        String username,
        String email,
        RoleName role,
        UserStatus userStatus,
        CompanyStatus companyStatus,
        String message
) {
}
