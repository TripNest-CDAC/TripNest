package com.tripnest.crud.repository;
import com.tripnest.crud.entity.PackageImage;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface PackageImageRepository extends JpaRepository<PackageImage, Integer> {
    List<PackageImage> findAllByTravelPackagePackageIdOrderByImageIdAsc(Integer packageId);
}
