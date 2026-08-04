package com.tripnest.auth.controller;

import com.tripnest.auth.config.OpenApiConfig;
import com.tripnest.auth.dto.CurrentUserResponse;
import com.tripnest.auth.dto.UpdateProfileRequest;
import com.tripnest.auth.service.AuthenticationService;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.*;
import java.util.UUID;

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

    @PutMapping("/me")
    @Operation(
            summary = "Update the logged-in user's profile",
            security = @SecurityRequirement(
                    name = OpenApiConfig.BEARER_AUTH
            )
    )
    public ResponseEntity<CurrentUserResponse> updateCurrentUser(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody UpdateProfileRequest request) {

        CurrentUserResponse response = authenticationService
                .updateCurrentUser(jwt.getSubject(), request);

        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/me/profile-image", consumes = "multipart/form-data")
    public ResponseEntity<CurrentUserResponse> uploadProfileImage(
            @AuthenticationPrincipal Jwt jwt, @RequestPart("image") MultipartFile image) throws Exception {
        if (image.isEmpty() || image.getContentType() == null || !image.getContentType().startsWith("image/")) {
            throw new IllegalArgumentException("Select a valid image file");
        }
        Path directory = Path.of("uploads", "profiles").toAbsolutePath();
        Files.createDirectories(directory);
        String original = image.getOriginalFilename() == null ? ".jpg" : image.getOriginalFilename();
        String extension = original.contains(".") ? original.substring(original.lastIndexOf('.')) : ".jpg";
        String imagePath = "/uploads/profiles/" + UUID.randomUUID() + extension;
        Files.copy(image.getInputStream(), directory.resolve(imagePath.substring(imagePath.lastIndexOf('/') + 1)), StandardCopyOption.REPLACE_EXISTING);
        return ResponseEntity.ok(authenticationService.updateProfileImage(jwt.getSubject(), imagePath));
    }

    @DeleteMapping("/me/profile-image")
    public ResponseEntity<CurrentUserResponse> removeProfileImage(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(authenticationService.updateProfileImage(jwt.getSubject(), null));
    }
}
