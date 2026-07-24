package com.tripnest.auth.dto;

import com.tripnest.auth.entity.RoleName;

public record AuthResponse(
        String accessToken,
        String tokenType,
        long expiresIn,
        Integer userId,
        String username,
        RoleName role
) {
}
