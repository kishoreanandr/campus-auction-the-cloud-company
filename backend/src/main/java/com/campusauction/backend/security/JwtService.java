package com.campusauction.backend.security;

import java.time.Instant;
import java.util.Date;
import java.security.SecureRandom;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.campusauction.backend.user.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);

    private final SecretKey signingKey;
    private final long expirationMillis;

    public JwtService(
            @Value("${app.jwt.secret:}") String secret,
            @Value("${app.jwt.expiration-ms:3600000}") long expirationMillis
    ) {
        if (expirationMillis <= 0) {
            throw new IllegalStateException(
                    "JWT_EXPIRATION_MS must be greater than zero"
            );
        }

        if (secret.isBlank()) {
            byte[] randomKey = new byte[32];
            new SecureRandom().nextBytes(randomKey);
            this.signingKey = Keys.hmacShaKeyFor(randomKey);
            logger.warn(
                    "JWT_SECRET is not configured. Using a random, temporary signing key; "
                            + "tokens will stop working after restart. Configure JWT_SECRET outside source control."
            );
        } else {
            try {
                this.signingKey = Keys.hmacShaKeyFor(
                        Decoders.BASE64.decode(secret)
                );
            } catch (IllegalArgumentException exception) {
                throw new IllegalStateException(
                        "JWT_SECRET must be a Base64-encoded key of at least 32 bytes",
                        exception
                );
            }
        }
        this.expirationMillis = expirationMillis;
    }

    public String generateToken(User user) {
        Instant issuedAt = Instant.now();
        return Jwts.builder()
                .subject(user.getEmail())
                .claim("uid", user.getId())
                .claim("role", user.getRole().name())
                .issuedAt(Date.from(issuedAt))
                .expiration(Date.from(issuedAt.plusMillis(expirationMillis)))
                .signWith(signingKey)
                .compact();
    }

    public Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public long getExpirationSeconds() {
        return expirationMillis / 1000;
    }
}
