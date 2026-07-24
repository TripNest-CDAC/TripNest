package com.tripnest.auth.repository;

import com.tripnest.auth.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Integer> {

    Optional<Company> findByUserUserId(Integer userId);

    boolean existsByCompanyName(String companyName);

    boolean existsByRegistrationNumber(String registrationNumber);
}
