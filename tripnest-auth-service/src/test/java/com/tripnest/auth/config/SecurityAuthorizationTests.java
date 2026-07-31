package com.tripnest.auth.config;

import com.tripnest.auth.controller.AdminCompanyController;
import com.tripnest.auth.controller.UserController;
import com.tripnest.auth.service.AuthenticationService;
import com.tripnest.auth.service.CompanyAdminService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = {
                UserController.class,
                AdminCompanyController.class
        },
        properties = {
                "app.jwt.secret=AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA=",
                "app.cors.allowed-origins=http://localhost:5173"
        }
)
@Import(SecurityConfig.class)
class SecurityAuthorizationTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthenticationService authenticationService;

    @MockitoBean
    private CompanyAdminService companyAdminService;

    @Test
    void currentUserRejectsAnonymousRequest() throws Exception {
        mockMvc.perform(get("/api/users/me"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void adminEndpointRejectsTouristToken() throws Exception {
        mockMvc.perform(get("/api/admin/companies/pending")
                        .with(jwt().authorities(
                                new SimpleGrantedAuthority("ROLE_TOURIST")
                        )))
                .andExpect(status().isForbidden());
    }

    @Test
    void adminEndpointAcceptsAdminToken() throws Exception {
        when(companyAdminService.getPendingCompanies())
                .thenReturn(List.of());

        mockMvc.perform(get("/api/admin/companies/pending")
                        .with(jwt().authorities(
                                new SimpleGrantedAuthority("ROLE_ADMIN")
                        )))
                .andExpect(status().isOk());
    }
}
