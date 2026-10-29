package com.userhub.service;

import com.userhub.dto.AuthResponse;
import com.userhub.dto.LoginRequest;
import com.userhub.dto.RegisterRequest;
import com.userhub.dto.UserResponse;
import com.userhub.entity.AuthToken;
import com.userhub.entity.Role;
import com.userhub.entity.User;
import com.userhub.exception.ApiException;
import com.userhub.repository.AuthTokenRepository;
import com.userhub.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final AuthTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final long ttlHours;

    public AuthService(UserRepository userRepository, AuthTokenRepository tokenRepository,
                       PasswordEncoder passwordEncoder, @Value("${app.token.ttl-hours:24}") long ttlHours) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.ttlHours = ttlHours;
    }

    @Transactional
    public UserResponse register(RegisterRequest req) {
        String email = req.email().trim().toLowerCase();
        if (userRepository.existsByEmail(email)) {
            throw new ApiException(HttpStatus.CONFLICT, "Email is already registered");
        }
        User user = new User(req.fullName().trim(), email, passwordEncoder.encode(req.password()), Role.USER);
        return UserResponse.from(userRepository.save(user));
    }

    @Transactional
    public AuthResponse login(LoginRequest req) {
        User user = userRepository.findByEmail(req.email().trim().toLowerCase())
                .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "Invalid email or password"));
        if (!passwordEncoder.matches(req.password(), user.getPassword())) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
        }
        String token = UUID.randomUUID() + UUID.randomUUID().toString().replace("-", "");
        tokenRepository.save(new AuthToken(token, user, LocalDateTime.now().plusHours(ttlHours)));
        return new AuthResponse(token, UserResponse.from(user));
    }

    /** Server-side logout: deletes the token so it can never be used again. */
    @Transactional
    public void logout(String token) {
        tokenRepository.findByToken(token).ifPresent(tokenRepository::delete);
    }

    /** Resolves a bearer token to its user, or throws 401. */
    @Transactional
    public User authenticate(String token) {
        AuthToken t = tokenRepository.findByToken(token)
                .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "Invalid or expired token"));
        if (t.getExpiresAt().isBefore(LocalDateTime.now())) {
            tokenRepository.delete(t);
            throw new ApiException(HttpStatus.UNAUTHORIZED, "Token has expired");
        }
        return t.getUser();
    }
}
