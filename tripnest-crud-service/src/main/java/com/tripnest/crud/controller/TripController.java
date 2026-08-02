package com.tripnest.crud.controller;
import com.tripnest.crud.dto.*; import com.tripnest.crud.service.TripService;
import jakarta.validation.Valid; import org.springframework.http.*; import org.springframework.security.core.annotation.AuthenticationPrincipal; import org.springframework.security.oauth2.jwt.Jwt; import org.springframework.web.bind.annotation.*; import java.util.List;
@RestController @RequestMapping("/api/trips")
public class TripController { private final TripService service; public TripController(TripService service){this.service=service;}
 @PostMapping public ResponseEntity<TripResponse> create(@Valid @RequestBody CreateTripRequest request,@AuthenticationPrincipal Jwt jwt){return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request,jwt));}
 @GetMapping public ResponseEntity<List<TripResponse>> list(@RequestParam(required=false) Integer packageId){return ResponseEntity.ok(service.list(packageId));}
 @GetMapping("/available") public ResponseEntity<List<TripResponse>> available(){return ResponseEntity.ok(service.available());}
 @PutMapping("/{tripId}") public ResponseEntity<TripResponse> update(@PathVariable Integer tripId,@Valid @RequestBody UpdateTripRequest request,@AuthenticationPrincipal Jwt jwt){return ResponseEntity.ok(service.update(tripId,request,jwt));}
 @DeleteMapping("/{tripId}") public ResponseEntity<Void> delete(@PathVariable Integer tripId,@AuthenticationPrincipal Jwt jwt){service.delete(tripId,jwt);return ResponseEntity.noContent().build();}
}
