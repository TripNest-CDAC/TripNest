package com.tripnest.crud.controller;

import com.tripnest.crud.dto.DestinationResponse;
import com.tripnest.crud.service.DestinationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/destinations")
public class DestinationController {
    private final DestinationService destinationService;
    public DestinationController(DestinationService destinationService) { this.destinationService = destinationService; }

    @GetMapping("/search")
    public ResponseEntity<List<DestinationResponse>> search(@RequestParam(required = false) String query) {
        return ResponseEntity.ok(destinationService.search(query));
    }
}
