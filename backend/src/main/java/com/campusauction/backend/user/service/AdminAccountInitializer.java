package com.campusauction.backend.user.service;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.campusauction.backend.user.entity.Role;
import com.campusauction.backend.user.entity.User;
import com.campusauction.backend.user.repository.UserRepository;

@Component
public class AdminAccountInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final String name;
    private final String email;
    private final String password;

    public AdminAccountInitializer(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            @Value("${app.admin.name:}") String name,
            @Value("${app.admin.email:}") String email,
            @Value("${app.admin.password:}") String password
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (name.isBlank() && email.isBlank() && password.isBlank()) {
            return;
        }
        if (name.isBlank() || email.isBlank() || password.isBlank()) {
            throw new IllegalStateException(
                    "ADMIN_NAME, ADMIN_EMAIL, and ADMIN_PASSWORD must all be set"
            );
        }
        if (password.length() < 8
                || password.getBytes(StandardCharsets.UTF_8).length > 72) {
            throw new IllegalStateException(
                    "ADMIN_PASSWORD must contain at least 8 characters and no more than 72 UTF-8 bytes"
            );
        }

        String normalizedEmail = email.trim().toLowerCase(Locale.ROOT);
        User existing = userRepository
                .findByEmailIgnoreCase(normalizedEmail)
                .orElse(null);
        if (existing != null) {
            if (existing.getRole() != Role.ADMIN) {
                throw new IllegalStateException(
                        "Configured administrator email is already assigned to a non-admin account"
                );
            }
            return;
        }

        User administrator = new User();
        administrator.setName(name.trim());
        administrator.setEmail(normalizedEmail);
        administrator.setPassword(passwordEncoder.encode(password));
        administrator.setRole(Role.ADMIN);
        userRepository.save(administrator);
    }
}
