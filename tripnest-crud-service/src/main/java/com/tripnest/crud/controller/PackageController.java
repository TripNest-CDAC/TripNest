package com.tripnest.crud.controller;

import com.tripnest.crud.config.OpenApiConfig;
import com.tripnest.crud.dto.CreatePackageRequest;
import com.tripnest.crud.dto.PackageResponse;
import com.tripnest.crud.dto.UpdatePackageRequest;
import com.tripnest.crud.service.PackageService;
import com.tripnest.crud.service.ImageStorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/packages")
@Tag(name = "Travel Packages", description = "Travel package CRUD operations")
@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH)
public class PackageController {

    private final PackageService packageService;
    private final ImageStorageService imageStorageService;

    public PackageController(PackageService packageService, ImageStorageService imageStorageService) {
        this.packageService = packageService;
        this.imageStorageService = imageStorageService;
    }

    @PostMapping
    @Operation(summary = "Create a travel package (COMPANY only)")
    public ResponseEntity<PackageResponse> createPackage(
            @Valid @RequestBody CreatePackageRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(packageService.createPackage(request, jwt));
    }

    @GetMapping
    @Operation(summary = "List travel packages for the logged-in role")
    public ResponseEntity<List<PackageResponse>> getPackages(
            @RequestParam(required = false) String destination,
            @RequestParam(required = false) Integer destinationId,
            @AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(jwt == null
                ? packageService.getAvailablePackages(destination, destinationId)
                : packageService.getPackages(destination, destinationId, jwt));
    }

    @GetMapping("/available")
    @Operation(summary = "Search only active packages with available upcoming trips")
    public ResponseEntity<List<PackageResponse>> availablePackages(
            @RequestParam(required = false) String destination,
            @RequestParam(required = false) Integer destinationId) {
        return ResponseEntity.ok(packageService.getAvailablePackages(destination, destinationId));
    }

    @PostMapping(value = "/{packageId}/images", consumes = "multipart/form-data")
    @Operation(summary = "Upload multiple package images; mark the first as thumbnail when requested")
    public ResponseEntity<List<String>> uploadImages(
            @PathVariable Integer packageId,
            @RequestPart("images") List<MultipartFile> images,
            @RequestParam(defaultValue = "false") boolean thumbnail,
            @AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(imageStorageService.savePackageImages(
                packageService.packageForManagement(packageId, jwt), images, thumbnail));
    }

    @GetMapping("/{packageId}")
    @Operation(summary = "Get one travel package")
    public ResponseEntity<PackageResponse> getPackage(
            @PathVariable Integer packageId,
            @AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(jwt == null
                ? packageService.getAvailablePackage(packageId)
                : packageService.getPackage(packageId, jwt));
    }

    @PutMapping("/{packageId}")
    @Operation(summary = "Update a travel package")
    public ResponseEntity<PackageResponse> updatePackage(
            @PathVariable Integer packageId,
            @Valid @RequestBody UpdatePackageRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(packageService.updatePackage(
                packageId, request, jwt));
    }

    @DeleteMapping("/{packageId}")
    @Operation(summary = "Delete a travel package")
    public ResponseEntity<Void> deletePackage(
            @PathVariable Integer packageId,
            @AuthenticationPrincipal Jwt jwt) {
        packageService.deletePackage(packageId, jwt);
        return ResponseEntity.noContent().build();
    }
}
