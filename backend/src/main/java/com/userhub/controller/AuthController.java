package com.userhub.controller;

import com.userhub.dto.AuthResponse;
import com.userhub.dto.LoginRequest;
import com.userhub.dto.RegisterRequest;
import com.userhub.dto.UserResponse;
import com.userhub.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(req));
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest req) {
        return authService.login(req);
    }

    // Protected by AuthInterceptor; the "token" attribute is set there.
    @PostMapping("/logout")
    public Map<String, String> logout(@RequestAttribute("token") String token) {
        authService.logout(token);
        return Map.of("message", "Logged out successfully");
    }
}
