package com.tripnest.auth.service;

import com.tripnest.auth.dto.CompanyAdminResponse;
import com.tripnest.auth.entity.Company;
import com.tripnest.auth.entity.CompanyStatus;
import com.tripnest.auth.exception.ResourceNotFoundException;
import com.tripnest.auth.repository.CompanyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CompanyAdminService {

    private final CompanyRepository companyRepository;

    public CompanyAdminService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Transactional(readOnly = true)
    public List<CompanyAdminResponse> getPendingCompanies() {
        return companyRepository
                .findAllByStatusOrderByCompanyIdAsc(CompanyStatus.PENDING)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<CompanyAdminResponse> getAllCompanies() {
        return companyRepository.findAllByOrderByCompanyIdAsc().stream().map(this::toResponse).toList();
    }

    @Transactional
    public CompanyAdminResponse approveCompany(Integer companyId) {
        return updateStatus(companyId, CompanyStatus.APPROVED);
    }

    @Transactional
    public CompanyAdminResponse suspendCompany(Integer companyId) {
        return updateStatus(companyId, CompanyStatus.SUSPENDED);
    }

    private CompanyAdminResponse updateStatus(
            Integer companyId,
            CompanyStatus status) {

        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Company was not found"
                ));

        company.setStatus(status);
        Company savedCompany = companyRepository.save(company);

        return toResponse(savedCompany);
    }

    private CompanyAdminResponse toResponse(Company company) {
        return new CompanyAdminResponse(
                company.getCompanyId(),
                company.getUser().getUserId(),
                company.getCompanyName(),
                company.getRegistrationNumber(),
                company.getCompanyAddress(),
                company.getStatus(),
                company.getUser().getUsername(),
                company.getUser().getEmail()
        );
    }
}
