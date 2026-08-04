package com.tripnest.crud.repository;

import com.tripnest.crud.entity.Destination;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DestinationRepository extends JpaRepository<Destination, Integer> {
    Optional<Destination> findByCityNameIgnoreCaseAndActiveTrue(String cityName);
    Optional<Destination> findByCityNameIgnoreCaseAndStateNameIgnoreCase(String cityName, String stateName);
    List<Destination> findAllByOrderByCityNameAscStateNameAsc();

    @Query("select d from Destination d where d.active = true and exists (" +
            "select t.tripId from Trip t where t.travelPackage.destinationInfo = d " +
            "and t.travelPackage.status = 'ACTIVE' and t.tripStatus = 'UPCOMING' " +
            "and t.startDate >= :today and t.seatsAvailable > 0) " +
            "and (:query is null or lower(d.cityName) like lower(concat('%', :query, '%'))) " +
            "order by d.cityName")
    List<Destination> findAvailableDestinations(@Param("query") String query, @Param("today") LocalDate today, Pageable pageable);
}
