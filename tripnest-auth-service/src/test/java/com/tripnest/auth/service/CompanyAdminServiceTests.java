package com.tripnest.auth.service;

import com.tripnest.auth.dto.CompanyAdminResponse;
import com.tripnest.auth.entity.Company;
import com.tripnest.auth.entity.CompanyStatus;
import com.tripnest.auth.entity.UserAccount;
import com.tripnest.auth.exception.ResourceNotFoundException;
import com.tripnest.auth.repository.CompanyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CompanyAdminServiceTests {

    @Mock
    private CompanyRepository companyRepository;

    private CompanyAdminService companyAdminService;

    @BeforeEach
    void setUp() {
        companyAdminService = new CompanyAdminService(companyRepository);
    }

    @Test
    void listsOnlyPendingCompanies() {
        Company company = company(31, CompanyStatus.PENDING);
        when(companyRepository.findAllByStatusOrderByCompanyIdAsc(
                CompanyStatus.PENDING
        )).thenReturn(List.of(company));

        List<CompanyAdminResponse> response =
                companyAdminService.getPendingCompanies();

        assertThat(response).hasSize(1);
        assertThat(response.getFirst().companyId()).isEqualTo(31);
        assertThat(response.getFirst().status())
                .isEqualTo(CompanyStatus.PENDING);
    }

    @Test
    void approvesCompany() {
        Company company = company(31, CompanyStatus.PENDING);
        when(companyRepository.findById(31))
                .thenReturn(Optional.of(company));
        when(companyRepository.save(company)).thenReturn(company);

        CompanyAdminResponse response =
                companyAdminService.approveCompany(31);

        assertThat(response.status()).isEqualTo(CompanyStatus.APPROVED);
        verify(companyRepository).save(company);
    }

    @Test
    void suspendsCompany() {
        Company company = company(31, CompanyStatus.APPROVED);
        when(companyRepository.findById(31))
                .thenReturn(Optional.of(company));
        when(companyRepository.save(company)).thenReturn(company);

        CompanyAdminResponse response =
                companyAdminService.suspendCompany(31);

        assertThat(response.status()).isEqualTo(CompanyStatus.SUSPENDED);
        verify(companyRepository).save(company);
    }

    @Test
    void rejectsUnknownCompanyId() {
        when(companyRepository.findById(999))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                companyAdminService.approveCompany(999))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Company was not found");
    }

    private Company company(Integer companyId, CompanyStatus status) {
        UserAccount user = new UserAccount();
        user.setUserId(41);
        user.setUsername("company1");
        user.setEmail("company@example.com");

        Company company = new Company();
        company.setCompanyId(companyId);
        company.setUser(user);
        company.setCompanyName("TripNest Demo Travels");
        company.setRegistrationNumber("REG-001");
        company.setCompanyAddress("Pune");
        company.setStatus(status);
        return company;
    }
}
