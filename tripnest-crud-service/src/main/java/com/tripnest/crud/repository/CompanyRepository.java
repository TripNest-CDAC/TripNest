package com.tripnest.crud.repository;

import com.tripnest.crud.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Integer> {
    Optional<Company> findByUserId(Integer userId);
}
