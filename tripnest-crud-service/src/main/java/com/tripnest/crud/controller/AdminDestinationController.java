package com.tripnest.crud.controller;

import com.tripnest.crud.dto.AdminDestinationResponse;
import com.tripnest.crud.dto.DestinationManagementRequest;
import com.tripnest.crud.dto.DestinationStatusRequest;
import com.tripnest.crud.service.DestinationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/destinations")
public class AdminDestinationController {
    private final DestinationService destinationService;
    public AdminDestinationController(DestinationService destinationService) { this.destinationService = destinationService; }

    @GetMapping public List<AdminDestinationResponse> list() { return destinationService.listForAdmin(); }
    @PostMapping public ResponseEntity<AdminDestinationResponse> create(@Valid @RequestBody DestinationManagementRequest request) { return ResponseEntity.status(HttpStatus.CREATED).body(destinationService.create(request)); }
    @PutMapping("/{destinationId}") public AdminDestinationResponse update(@PathVariable Integer destinationId, @Valid @RequestBody DestinationManagementRequest request) { return destinationService.update(destinationId, request); }
    @PatchMapping("/{destinationId}/status") public AdminDestinationResponse updateStatus(@PathVariable Integer destinationId, @RequestBody DestinationStatusRequest request) { return destinationService.updateStatus(destinationId, request.active()); }
    @DeleteMapping("/{destinationId}") public ResponseEntity<Void> delete(@PathVariable Integer destinationId) { destinationService.delete(destinationId); return ResponseEntity.noContent().build(); }
}
