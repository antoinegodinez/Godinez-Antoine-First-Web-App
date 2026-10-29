package com.userhub.config;

import com.userhub.entity.Role;
import com.userhub.entity.User;
import com.userhub.exception.ApiException;
import com.userhub.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/** Validates the Bearer token and enforces role-based access for protected endpoints. */
@Component
public class AuthInterceptor implements HandlerInterceptor {
    private final AuthService authService;

    public AuthInterceptor(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) {
        if ("OPTIONS".equalsIgnoreCase(req.getMethod())) return true; // CORS preflight

        String header = req.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "Missing or invalid Authorization header");
        }
        String token = header.substring(7).trim();
        User user = authService.authenticate(token);

        if (req.getRequestURI().startsWith("/api/admin") && user.getRole() != Role.ADMIN) {
            throw new ApiException(HttpStatus.FORBIDDEN, "Admin access required");
        }
        req.setAttribute("currentUser", user);
        req.setAttribute("token", token);
        return true;
    }
}
