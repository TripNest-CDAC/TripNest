package com.tripnest.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateProfileRequest(

        @NotBlank
        @Size(max = 100)
        String firstName,

        @Size(max = 100)
        String lastName,

        @Size(max = 15)
        String phone,

        String address,

        @Size(max = 150)
        String companyName,

        String companyAddress
) {
}
