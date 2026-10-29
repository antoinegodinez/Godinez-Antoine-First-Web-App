package com.userhub.controller;

import com.userhub.dto.UpdateProfileRequest;
import com.userhub.dto.UserResponse;
import com.userhub.entity.Role;
import com.userhub.exception.ApiException;
import com.userhub.dto.*;
import com.userhub.entity.User;
import com.userhub.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {
    private final UserService userService;

    public DashboardController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping({"", "/me"})
    public UserResponse me(@RequestAttribute("currentUser") User user) {
        return UserResponse.from(user);
    }

    @GetMapping("/users")
    public java.util.List<UserResponse> users(@RequestAttribute("currentUser") User user) {
        if (user.getRole() != Role.ADMIN) {
            throw new ApiException(org.springframework.http.HttpStatus.FORBIDDEN, "Admin access required");
        }
        return userService.findAll();
    }

    @PutMapping("/me")
    public UserResponse updateMe(@RequestAttribute("currentUser") User user,
                                 @Valid @RequestBody UpdateProfileRequest req) {
        return userService.updateFullName(user.getId(), req.fullName());
    }
}
