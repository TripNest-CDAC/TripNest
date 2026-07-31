package com.tripnest.auth.controller;

import com.tripnest.auth.config.OpenApiConfig;
import com.tripnest.auth.dto.CurrentUserResponse;
import com.tripnest.auth.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "Authenticated user operations")
public class UserController {

    private final AuthenticationService authenticationService;

    public UserController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @GetMapping("/me")
    @Operation(
            summary = "Get the logged-in user's profile",
            security = @SecurityRequirement(
                    name = OpenApiConfig.BEARER_AUTH
            )
    )
    public ResponseEntity<CurrentUserResponse> currentUser(
            @AuthenticationPrincipal Jwt jwt) {

        CurrentUserResponse response =
                authenticationService.getCurrentUser(jwt.getSubject());

        return ResponseEntity.ok(response);
    }
}
