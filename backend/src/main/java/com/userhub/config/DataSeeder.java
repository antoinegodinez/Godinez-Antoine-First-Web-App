package com.userhub.config;

import com.userhub.entity.Role;
import com.userhub.entity.User;
import com.userhub.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/** Seeds a default ADMIN account on startup (only if it does not already exist). */
@Component
public class DataSeeder implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final String email, password, fullName;

    public DataSeeder(UserRepository userRepository, PasswordEncoder passwordEncoder,
                      @Value("${app.admin.email}") String email,
                      @Value("${app.admin.password}") String password,
                      @Value("${app.admin.full-name}") String fullName) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.email = email;
        this.password = password;
        this.fullName = fullName;
    }

    @Override
    public void run(String... args) {
        if (!userRepository.existsByEmail(email)) {
            userRepository.save(new User(fullName, email, passwordEncoder.encode(password), Role.ADMIN));
            System.out.println("[UserHub] Seeded default ADMIN: " + email);
        }
    }
}
