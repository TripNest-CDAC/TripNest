package com.tripnest.auth.repository;

import com.tripnest.auth.entity.Company;
import com.tripnest.auth.entity.CompanyStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Integer> {

    Optional<Company> findByUserUserId(Integer userId);

    List<Company> findAllByStatusOrderByCompanyIdAsc(CompanyStatus status);

    boolean existsByCompanyName(String companyName);

    boolean existsByRegistrationNumber(String registrationNumber);
}
