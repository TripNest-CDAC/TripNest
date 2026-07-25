package com.tripnest.auth.dto;

import com.tripnest.auth.entity.CompanyStatus;
import com.tripnest.auth.entity.RoleName;
import com.tripnest.auth.entity.UserStatus;

public record CurrentUserResponse(
        Integer userId,
        String username,
        String firstName,
        String lastName,
        String email,
        String phone,
        RoleName role,
        UserStatus userStatus,
        CompanyStatus companyStatus
) {
}
