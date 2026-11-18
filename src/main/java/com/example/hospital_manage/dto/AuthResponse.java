package com.example.hospital_manage.dto;

import com.example.hospital_manage.model.Role;

public record AuthResponse(
        String token,
        String tokenType,
        long expiresInMs,
        String username,
        String fullName,
        Role role
) {}