package com.tripnest.auth.service;

import com.tripnest.auth.dto.AuthResponse;
import com.tripnest.auth.dto.LoginRequest;
import com.tripnest.auth.dto.RegisterRequest;
import com.tripnest.auth.dto.RegisterResponse;
import com.tripnest.auth.entity.Company;
import com.tripnest.auth.entity.CompanyStatus;
import com.tripnest.auth.entity.Role;
import com.tripnest.auth.entity.RoleName;
import com.tripnest.auth.entity.UserAccount;
import com.tripnest.auth.entity.UserStatus;
import com.tripnest.auth.repository.CompanyRepository;
import com.tripnest.auth.repository.RoleRepository;
import com.tripnest.auth.repository.UserAccountRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
public class AuthenticationService {

    private final UserAccountRepository userRepository;
    private final RoleRepository roleRepository;
    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthenticationService(
            UserAccountRepository userRepository,
            RoleRepository roleRepository,
            CompanyRepository companyRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.companyRepository = companyRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Transactional
    public RegisterResponse register(RegisterRequest request) {
        RoleName requestedRole = request.role();

        if (requestedRole != RoleName.TOURIST
                && requestedRole != RoleName.COMPANY) {
            throw new IllegalArgumentException(
                    "Only TOURIST and COMPANY registration is allowed"
            );
        }

        String username = request.username().trim();
        String email = request.email()
                .trim()
                .toLowerCase(Locale.ROOT);
        String phone = normalizeOptional(request.phone());

        validateUserDuplicates(username, email, phone);

        Role role = roleRepository.findByRoleName(requestedRole)
                .orElseThrow(() -> new IllegalStateException(
                        "Required role is missing from the database"
                ));

        UserAccount user = new UserAccount();
        user.setRole(role);
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setFirstName(request.firstName().trim());
        user.setLastName(normalizeOptional(request.lastName()));
        user.setEmail(email);
        user.setPhone(phone);
        user.setAddress(normalizeOptional(request.address()));
        user.setStatus(UserStatus.ACTIVE);

        UserAccount savedUser = userRepository.save(user);

        CompanyStatus companyStatus = null;
        String message = "Tourist registration successful";

        if (requestedRole == RoleName.COMPANY) {
            Company company = createCompany(request, savedUser);
            companyRepository.save(company);

            companyStatus = CompanyStatus.PENDING;
            message = "Company registration successful and awaiting approval";
        }

        return new RegisterResponse(
                savedUser.getUserId(),
                savedUser.getUsername(),
                savedUser.getEmail(),
                savedUser.getRole().getRoleName(),
                savedUser.getStatus(),
                companyStatus,
                message
        );
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        String username = request.username().trim();

        UserAccount user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BadCredentialsException(
                        "Invalid username or password"
                ));

        if (!passwordEncoder.matches(
                request.password(),
                user.getPassword())) {
            throw new BadCredentialsException(
                    "Invalid username or password"
            );
        }

        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new DisabledException(
                    "User account is not active"
            );
        }

        if (user.getRole().getRoleName() == RoleName.COMPANY) {
            validateCompanyApproval(user);
        }

        String token = jwtService.generateToken(user);

        return new AuthResponse(
                token,
                "Bearer",
                jwtService.getExpirationSeconds(),
                user.getUserId(),
                user.getUsername(),
                user.getRole().getRoleName()
        );
    }

    private void validateUserDuplicates(
            String username,
            String email,
            String phone) {

        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException(
                    "Username is already registered"
            );
        }

        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException(
                    "Email is already registered"
            );
        }

        if (phone != null && userRepository.existsByPhone(phone)) {
            throw new IllegalArgumentException(
                    "Phone number is already registered"
            );
        }
    }

    private Company createCompany(
            RegisterRequest request,
            UserAccount savedUser) {

        String companyName = requireText(
                request.companyName(),
                "Company name is required"
        );

        String registrationNumber = requireText(
                request.registrationNumber(),
                "Company registration number is required"
        );

        if (companyRepository.existsByCompanyName(companyName)) {
            throw new IllegalArgumentException(
                    "Company name is already registered"
            );
        }

        if (companyRepository.existsByRegistrationNumber(
                registrationNumber)) {
            throw new IllegalArgumentException(
                    "Company registration number is already registered"
            );
        }

        Company company = new Company();
        company.setUser(savedUser);
        company.setCompanyName(companyName);
        company.setRegistrationNumber(registrationNumber);
        company.setCompanyAddress(
                normalizeOptional(request.companyAddress())
        );
        company.setStatus(CompanyStatus.PENDING);

        return company;
    }

    private void validateCompanyApproval(UserAccount user) {
        Company company = companyRepository
                .findByUserUserId(user.getUserId())
                .orElseThrow(() -> new DisabledException(
                        "Company profile was not found"
                ));

        if (company.getStatus() != CompanyStatus.APPROVED) {
            throw new DisabledException(
                    "Company account is not approved"
            );
        }
    }

    private String requireText(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(message);
        }

        return value.trim();
    }

    private String normalizeOptional(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return value.trim();
    }
}
