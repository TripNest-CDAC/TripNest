package com.tripnest.auth.dto;

import com.tripnest.auth.entity.RoleName;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegisterRequest(

        @NotBlank
        @Size(max = 100)
        String username,

        @NotBlank
        @Size(min = 8, max = 72)
        String password,

        @NotBlank
        @Size(max = 100)
        String firstName,

        @Size(max = 100)
        String lastName,

        @NotBlank
        @Email
        @Size(max = 150)
        String email,

        @Size(max = 15)
        String phone,

        String address,

        @NotNull
        RoleName role,

        @Size(max = 150)
        String companyName,

        @Size(max = 100)
        String registrationNumber,

        String companyAddress
) {
}
