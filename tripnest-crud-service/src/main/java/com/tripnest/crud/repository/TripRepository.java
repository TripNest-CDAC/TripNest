package com.tripnest.crud.repository;
import com.tripnest.crud.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.time.LocalDate;
import com.tripnest.crud.entity.TripStatus;
public interface TripRepository extends JpaRepository<Trip, Integer> {
    List<Trip> findAllByTravelPackagePackageIdOrderByStartDateAsc(Integer packageId);
    List<Trip> findAllByTripStatusAndStartDateGreaterThanEqualAndSeatsAvailableGreaterThanOrderByStartDateAsc(
            TripStatus status, LocalDate startDate, Integer seatsAvailable);
}
