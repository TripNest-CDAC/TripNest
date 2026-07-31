package com.tripnest.auth.controller;

import com.tripnest.auth.config.OpenApiConfig;
import com.tripnest.auth.dto.CompanyAdminResponse;
import com.tripnest.auth.service.CompanyAdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/companies")
@Tag(name = "Admin Companies", description = "Admin-only company approval")
@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH)
public class AdminCompanyController {

    private final CompanyAdminService companyAdminService;

    public AdminCompanyController(CompanyAdminService companyAdminService) {
        this.companyAdminService = companyAdminService;
    }

    @GetMapping("/pending")
    @Operation(summary = "List companies waiting for approval")
    public ResponseEntity<List<CompanyAdminResponse>> pendingCompanies() {
        return ResponseEntity.ok(
                companyAdminService.getPendingCompanies()
        );
    }

    @PatchMapping("/{companyId}/approve")
    @Operation(summary = "Approve a company")
    public ResponseEntity<CompanyAdminResponse> approveCompany(
            @PathVariable Integer companyId) {

        return ResponseEntity.ok(
                companyAdminService.approveCompany(companyId)
        );
    }

    @PatchMapping("/{companyId}/suspend")
    @Operation(summary = "Suspend or reject a company")
    public ResponseEntity<CompanyAdminResponse> suspendCompany(
            @PathVariable Integer companyId) {

        return ResponseEntity.ok(
                companyAdminService.suspendCompany(companyId)
        );
    }
}
