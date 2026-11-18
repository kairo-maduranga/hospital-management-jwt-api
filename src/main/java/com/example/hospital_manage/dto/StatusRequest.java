package com.example.hospital_manage.dto;

import com.example.hospital_manage.model.AppointmentStatus;
import jakarta.validation.constraints.NotNull;

public record StatusRequest(
        @NotNull(message = "Status is required") AppointmentStatus status
) {}