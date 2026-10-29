package com.userhub.dto;

import jakarta.validation.constraints.*;

public record UpdateProfileRequest(
        @NotBlank(message = "Full name is required")
        @Size(min = 2, max = 100, message = "Full name must be 2-100 characters")
        String fullName) {}
