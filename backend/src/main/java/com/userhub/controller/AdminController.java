package com.userhub.controller;

import com.userhub.dto.UserResponse;
import com.userhub.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    // ADMIN role is enforced in AuthInterceptor for every /api/admin/** path.
    @GetMapping("/users")
    public List<UserResponse> users() {
        return userService.findAll();
    }
}
