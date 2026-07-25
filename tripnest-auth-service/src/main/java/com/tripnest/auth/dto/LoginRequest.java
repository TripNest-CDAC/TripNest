package com.tripnest.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(

        @NotBlank(message = "Username or email is required")
        @Size(max = 100)
        String username,

        @NotBlank(message = "Password is required")
        @Size(max = 72)
        String password
) {
}
