package com.example.hospital_manage.dto;

import com.example.hospital_manage.model.Role;

public record UserResponse(
        Long id,
        String username,
        String fullName,
        Role role
) {}