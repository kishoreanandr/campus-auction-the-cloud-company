package com.campusauction.backend.security;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Base64;

import org.junit.jupiter.api.Test;

import com.campusauction.backend.user.entity.Role;
import com.campusauction.backend.user.entity.User;

class JwtServiceTest {

    @Test
    void tokenContainsUserIdAndRole() {
        String secret = Base64.getEncoder().encodeToString(new byte[32]);
        JwtService jwtService = new JwtService(secret, 60_000);
        User user = new User();
        user.setId(42L);
        user.setEmail("bidder@example.edu");
        user.setRole(Role.BIDDER);

        var claims = jwtService.parseClaims(jwtService.generateToken(user));

        assertEquals("bidder@example.edu", claims.getSubject());
        assertEquals(42, ((Number) claims.get("uid")).longValue());
        assertEquals(Role.BIDDER.name(), claims.get("role"));
    }

    @Test
    void generatesTemporarySigningKeyWhenNoSecretIsConfigured() {
        JwtService jwtService = new JwtService("", 60_000);
        User user = new User();
        user.setId(42L);
        user.setEmail("bidder@example.edu");
        user.setRole(Role.BIDDER);

        assertEquals(
                "bidder@example.edu",
                jwtService.parseClaims(jwtService.generateToken(user)).getSubject()
        );
    }
}
