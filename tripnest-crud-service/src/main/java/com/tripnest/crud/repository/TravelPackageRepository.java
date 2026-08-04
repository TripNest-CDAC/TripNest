package com.tripnest.crud.repository;

import com.tripnest.crud.entity.PackageStatus;
import com.tripnest.crud.entity.TravelPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface TravelPackageRepository extends JpaRepository<TravelPackage, Integer> {
    boolean existsByDestinationInfoDestinationId(Integer destinationId);
    List<TravelPackage> findAllByCompanyCompanyIdOrderByPackageIdDesc(Integer companyId);
    List<TravelPackage> findAllByStatusOrderByPackageIdDesc(PackageStatus status);
    List<TravelPackage> findAllByStatusAndDestinationContainingIgnoreCaseOrderByPackageIdDesc(
            PackageStatus status, String destination);

    List<TravelPackage> findAllByStatusAndDestinationInfoDestinationIdOrderByPackageIdDesc(
            PackageStatus status, Integer destinationId);

    @Query("select distinct p from TravelPackage p join Trip t on t.travelPackage = p " +
            "where p.status = 'ACTIVE' and t.tripStatus = 'UPCOMING' " +
            "and t.startDate >= :today and t.seatsAvailable > 0 " +
            "and (:destination is null or lower(p.destination) like lower(concat('%', :destination, '%')) " +
            "or lower(p.packageName) like lower(concat('%', :destination, '%')))")
    List<TravelPackage> findAvailablePackages(LocalDate today, String destination);

    @Query("select distinct p from TravelPackage p join Trip t on t.travelPackage = p " +
            "where p.status = 'ACTIVE' and p.destinationInfo.destinationId = :destinationId " +
            "and t.tripStatus = 'UPCOMING' and t.startDate >= :today and t.seatsAvailable > 0")
    List<TravelPackage> findAvailablePackagesByDestinationId(@Param("today") LocalDate today, @Param("destinationId") Integer destinationId);
}
