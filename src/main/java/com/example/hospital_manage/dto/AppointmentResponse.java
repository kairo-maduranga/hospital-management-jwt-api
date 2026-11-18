package com.example.hospital_manage.dto;

import com.example.hospital_manage.model.AppointmentStatus;
import java.time.LocalDateTime;

public record AppointmentResponse(
        Long id,
        Long patientId,
        String patientName,
        Long doctorId,
        String doctorName,
        LocalDateTime appointmentDate,
        AppointmentStatus status
) {}