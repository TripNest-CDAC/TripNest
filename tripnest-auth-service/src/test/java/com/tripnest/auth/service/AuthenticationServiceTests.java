package com.tripnest.auth.service;

import com.tripnest.auth.dto.AuthResponse;
import com.tripnest.auth.dto.CurrentUserResponse;
import com.tripnest.auth.dto.LoginRequest;
import com.tripnest.auth.dto.RegisterRequest;
import com.tripnest.auth.dto.RegisterResponse;
import com.tripnest.auth.entity.Company;
import com.tripnest.auth.entity.CompanyStatus;
import com.tripnest.auth.entity.Role;
import com.tripnest.auth.entity.RoleName;
import com.tripnest.auth.entity.UserAccount;
import com.tripnest.auth.entity.UserStatus;
import com.tripnest.auth.exception.DuplicateResourceException;
import com.tripnest.auth.repository.CompanyRepository;
import com.tripnest.auth.repository.RoleRepository;
import com.tripnest.auth.repository.UserAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTests {

    @Mock
    private UserAccountRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        authenticationService = new AuthenticationService(
                userRepository,
                roleRepository,
                companyRepository,
                passwordEncoder,
                jwtService
        );
    }

    @Test
    void registersTouristWithHashedPasswordAndActiveStatus() {
        Role touristRole = role(RoleName.TOURIST);
        RegisterRequest request = touristRequest();

        when(roleRepository.findByRoleName(RoleName.TOURIST))
                .thenReturn(Optional.of(touristRole));
        when(passwordEncoder.encode("StrongPass123"))
                .thenReturn("$2a$10$hashedPassword");
        when(userRepository.save(any(UserAccount.class)))
                .thenAnswer(invocation -> {
                    UserAccount user = invocation.getArgument(0);
                    user.setUserId(11);
                    return user;
                });

        RegisterResponse response =
                authenticationService.register(request);

        ArgumentCaptor<UserAccount> userCaptor =
                ArgumentCaptor.forClass(UserAccount.class);
        verify(userRepository).save(userCaptor.capture());

        UserAccount savedUser = userCaptor.getValue();
        assertThat(savedUser.getPassword())
                .isEqualTo("$2a$10$hashedPassword");
        assertThat(savedUser.getStatus())
                .isEqualTo(UserStatus.ACTIVE);
        assertThat(savedUser.getEmail()).isEqualTo("tourist@example.com");
        assertThat(response.userId()).isEqualTo(11);
        assertThat(response.role()).isEqualTo(RoleName.TOURIST);
        assertThat(response.companyStatus()).isNull();
        verify(companyRepository, never()).save(any(Company.class));
    }

    @Test
    void rejectsDuplicateEmailWithConflictException() {
        when(userRepository.existsByEmail("tourist@example.com"))
                .thenReturn(true);

        assertThatThrownBy(() ->
                authenticationService.register(touristRequest()))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("Email is already registered");

        verify(userRepository, never()).save(any(UserAccount.class));
    }

    @Test
    void logsInUsingEmailAndReturnsJwt() {
        UserAccount user = user(RoleName.TOURIST, UserStatus.ACTIVE);
        LoginRequest request =
                new LoginRequest("TOURIST@EXAMPLE.COM", "StrongPass123");

        when(userRepository.findByUsernameOrEmail(
                "TOURIST@EXAMPLE.COM",
                "tourist@example.com"
        )).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(
                "StrongPass123",
                user.getPassword()
        )).thenReturn(true);
        when(jwtService.generateToken(user)).thenReturn("signed.jwt.token");
        when(jwtService.getExpirationSeconds()).thenReturn(3600L);

        AuthResponse response = authenticationService.login(request);

        assertThat(response.accessToken()).isEqualTo("signed.jwt.token");
        assertThat(response.tokenType()).isEqualTo("Bearer");
        assertThat(response.username()).isEqualTo("tourist1");
        assertThat(response.role()).isEqualTo(RoleName.TOURIST);
    }

    @Test
    void rejectsIncorrectPassword() {
        UserAccount user = user(RoleName.TOURIST, UserStatus.ACTIVE);
        LoginRequest request =
                new LoginRequest("tourist1", "WrongPassword");

        when(userRepository.findByUsernameOrEmail(
                "tourist1",
                "tourist1"
        )).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(
                "WrongPassword",
                user.getPassword()
        )).thenReturn(false);

        assertThatThrownBy(() -> authenticationService.login(request))
                .isInstanceOf(BadCredentialsException.class)
                .hasMessage("Invalid username/email or password");

        verify(jwtService, never()).generateToken(any(UserAccount.class));
    }

    @Test
    void rejectsInactiveUserLogin() {
        UserAccount user = user(RoleName.TOURIST, UserStatus.INACTIVE);

        when(userRepository.findByUsernameOrEmail(
                "tourist1",
                "tourist1"
        )).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(
                "StrongPass123",
                user.getPassword()
        )).thenReturn(true);

        assertThatThrownBy(() -> authenticationService.login(
                new LoginRequest("tourist1", "StrongPass123")
        ))
                .isInstanceOf(DisabledException.class)
                .hasMessage("User account is not active");

        verify(jwtService, never()).generateToken(any(UserAccount.class));
    }

    @Test
    void rejectsCompanyLoginUntilCompanyIsApproved() {
        UserAccount user = user(RoleName.COMPANY, UserStatus.ACTIVE);
        Company company = new Company();
        company.setUser(user);
        company.setStatus(CompanyStatus.PENDING);

        when(userRepository.findByUsernameOrEmail(
                "company1",
                "company1"
        )).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(
                "StrongPass123",
                user.getPassword()
        )).thenReturn(true);
        when(companyRepository.findByUserUserId(user.getUserId()))
                .thenReturn(Optional.of(company));

        assertThatThrownBy(() -> authenticationService.login(
                new LoginRequest("company1", "StrongPass123")
        ))
                .isInstanceOf(DisabledException.class)
                .hasMessage("Company account is not approved");

        verify(jwtService, never()).generateToken(any(UserAccount.class));
    }

    @Test
    void returnsSafeCurrentUserProfile() {
        UserAccount user = user(RoleName.TOURIST, UserStatus.ACTIVE);
        when(userRepository.findByUsername("tourist1"))
                .thenReturn(Optional.of(user));

        CurrentUserResponse response =
                authenticationService.getCurrentUser("tourist1");

        assertThat(response.userId()).isEqualTo(21);
        assertThat(response.email()).isEqualTo("tourist@example.com");
        assertThat(response.role()).isEqualTo(RoleName.TOURIST);
        assertThat(response.userStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(response.companyStatus()).isNull();
    }

    private RegisterRequest touristRequest() {
        return new RegisterRequest(
                "tourist1",
                "StrongPass123",
                "Test",
                "Tourist",
                "TOURIST@example.com",
                "9876543210",
                "Pune",
                RoleName.TOURIST,
                null,
                null,
                null
        );
    }

    private UserAccount user(
            RoleName roleName,
            UserStatus status) {

        UserAccount user = new UserAccount();
        user.setUserId(21);
        user.setRole(role(roleName));
        user.setUsername(
                roleName == RoleName.COMPANY
                        ? "company1"
                        : "tourist1"
        );
        user.setPassword("$2a$10$storedHash");
        user.setFirstName("Test");
        user.setLastName("User");
        user.setEmail(
                roleName == RoleName.COMPANY
                        ? "company@example.com"
                        : "tourist@example.com"
        );
        user.setPhone("9876543210");
        user.setStatus(status);

        return user;
    }

    private Role role(RoleName roleName) {
        Role role = new Role();
        role.setRoleId(roleName.ordinal() + 1);
        role.setRoleName(roleName);
        return role;
    }
}
