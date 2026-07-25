package com.tripnest.auth.config;

import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.jwt.Jwt;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SecurityConfigTests {

    private final SecurityConfig securityConfig = new SecurityConfig();

    @Test
    void acceptsA32ByteBase64JwtSecret() {
        String encodedSecret = Base64.getEncoder()
                .encodeToString(new byte[32]);

        SecretKey secretKey =
                securityConfig.jwtSecretKey(encodedSecret);

        assertThat(secretKey.getEncoded()).hasSize(32);
        assertThat(secretKey.getAlgorithm()).isEqualTo("HmacSHA256");
    }

    @Test
    void rejectsJwtSecretShorterThan32Bytes() {
        String encodedSecret = Base64.getEncoder()
                .encodeToString(new byte[16]);

        assertThatThrownBy(() ->
                securityConfig.jwtSecretKey(encodedSecret))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("JWT secret must contain at least 32 bytes");
    }

    @Test
    void convertsRoleClaimToSpringSecurityAuthority() {
        Instant now = Instant.now();
        Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "HS256")
                .issuer("tripnest-auth-service")
                .subject("tourist1")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(3600))
                .claim("role", "TOURIST")
                .build();

        var authentication = securityConfig
                .jwtAuthenticationConverter()
                .convert(jwt);

        assertThat(authentication).isNotNull();
        assertThat(authentication.getAuthorities())
                .extracting("authority")
                .contains("ROLE_TOURIST");
    }
}
