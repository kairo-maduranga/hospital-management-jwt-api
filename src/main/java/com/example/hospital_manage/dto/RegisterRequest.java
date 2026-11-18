package com.example.hospital_manage.dto;

import com.example.hospital_manage.model.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegisterRequest(
        @NotBlank(message = "Username is required") String username,
        @NotBlank(message = "Password is required") String password,
        @NotBlank(message = "Full name is required") String fullName,
        @NotNull(message = "Role is required") Role role
) {}