package com.tripnest.crud.service;

import com.tripnest.crud.dto.DestinationResponse;
import com.tripnest.crud.dto.AdminDestinationResponse;
import com.tripnest.crud.dto.DestinationManagementRequest;
import com.tripnest.crud.entity.Destination;
import com.tripnest.crud.exception.ResourceNotFoundException;
import com.tripnest.crud.repository.DestinationRepository;
import com.tripnest.crud.repository.TravelPackageRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class DestinationService {
    private final DestinationRepository destinationRepository;
    private final TravelPackageRepository packageRepository;
    public DestinationService(DestinationRepository destinationRepository, TravelPackageRepository packageRepository) { this.destinationRepository = destinationRepository; this.packageRepository = packageRepository; }

    @Transactional(readOnly = true)
    public List<DestinationResponse> search(String query) {
        String text = query == null || query.isBlank() ? null : query.trim();
        return destinationRepository.findAvailableDestinations(text, LocalDate.now(), PageRequest.of(0, 8))
                .stream().map(this::response).toList();
    }

    @Transactional(readOnly = true)
    public Destination availableDestination(Integer destinationId) {
        return destinationRepository.findAvailableDestinations(null, LocalDate.now(), PageRequest.of(0, 100))
                .stream().filter(destination -> destination.getDestinationId().equals(destinationId)).findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Destination is not available"));
    }

    @Transactional(readOnly = true)
    public List<AdminDestinationResponse> listForAdmin() {
        return destinationRepository.findAllByOrderByCityNameAscStateNameAsc().stream().map(this::adminResponse).toList();
    }

    @Transactional
    public AdminDestinationResponse create(DestinationManagementRequest request) {
        String city = request.cityName().trim();
        String state = request.stateName().trim();
        if (destinationRepository.findByCityNameIgnoreCaseAndStateNameIgnoreCase(city, state).isPresent()) {
            throw new IllegalArgumentException("This destination already exists");
        }
        Destination destination = new Destination();
        destination.setCityName(city);
        destination.setStateName(state);
        destination.setActive(true);
        return adminResponse(destinationRepository.save(destination));
    }

    @Transactional
    public AdminDestinationResponse update(Integer destinationId, DestinationManagementRequest request) {
        Destination destination = find(destinationId);
        String city = request.cityName().trim();
        String state = request.stateName().trim();
        destinationRepository.findByCityNameIgnoreCaseAndStateNameIgnoreCase(city, state)
                .filter(existing -> !existing.getDestinationId().equals(destinationId))
                .ifPresent(existing -> { throw new IllegalArgumentException("This destination already exists"); });
        destination.setCityName(city);
        destination.setStateName(state);
        return adminResponse(destinationRepository.save(destination));
    }

    @Transactional
    public AdminDestinationResponse updateStatus(Integer destinationId, boolean active) {
        Destination destination = find(destinationId);
        destination.setActive(active);
        return adminResponse(destinationRepository.save(destination));
    }

    @Transactional
    public void delete(Integer destinationId) {
        Destination destination = find(destinationId);
        if (packageRepository.existsByDestinationInfoDestinationId(destinationId)) {
            throw new IllegalArgumentException("This destination is used by a package. Deactivate it instead.");
        }
        destinationRepository.delete(destination);
    }

    private Destination find(Integer destinationId) {
        return destinationRepository.findById(destinationId)
                .orElseThrow(() -> new ResourceNotFoundException("Destination was not found"));
    }

    private DestinationResponse response(Destination destination) {
        return new DestinationResponse(destination.getDestinationId(), destination.getCityName(), destination.getStateName(),
                destination.getCityName() + ", " + destination.getStateName());
    }

    private AdminDestinationResponse adminResponse(Destination destination) {
        return new AdminDestinationResponse(destination.getDestinationId(), destination.getCityName(), destination.getStateName(), destination.isActive());
    }
}
