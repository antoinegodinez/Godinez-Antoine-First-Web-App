package com.userhub.controller;

import com.userhub.dto.UpdateProfileRequest;
import com.userhub.dto.UserResponse;
import com.userhub.entity.User;
import com.userhub.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class ProfileController {
    private final UserService userService;
    public ProfileController(UserService userService) { this.userService = userService; }

    @PutMapping("/me")
    public UserResponse update(@RequestAttribute("currentUser") User user,
                               @Valid @RequestBody UpdateProfileRequest request) {
        return userService.updateFullName(user.getId(), request.fullName());
    }
}
