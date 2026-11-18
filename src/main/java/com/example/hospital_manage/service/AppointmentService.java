package com.example.hospital_manage.service;

import com.example.hospital_manage.dto.AppointmentRequest;
import com.example.hospital_manage.dto.AppointmentResponse;
import com.example.hospital_manage.dto.StatusRequest;
import com.example.hospital_manage.exception.ApiException;
import com.example.hospital_manage.model.*;
import com.example.hospital_manage.repository.AppointmentRepository;
import com.example.hospital_manage.repository.PatientRepository;
import com.example.hospital_manage.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final UserRepository userRepository;

    public AppointmentResponse bookAppointment(AppointmentRequest request) {
        Patient patient = patientRepository.findById(request.patientId())
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Patient not found"));

        AppUser doctor = userRepository.findById(request.doctorId())
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Doctor not found"));

        if (doctor.getRole() != Role.DOCTOR) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Selected user is not a doctor");
        }

        Appointment appointment = Appointment.builder()
                .patient(patient)
                .doctor(doctor)
                .appointmentDate(request.appointmentDate())
                .status(AppointmentStatus.SCHEDULED)
                .build();

        return toResponse(appointmentRepository.save(appointment));
    }

    public List<AppointmentResponse> getAppointments(String username) {
        AppUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "User not found"));

        List<Appointment> list = (user.getRole() == Role.DOCTOR)
                ? appointmentRepository.findByDoctorId(user.getId())
                : appointmentRepository.findAll();

        return list.stream().map(this::toResponse).toList();
    }

    public AppointmentResponse updateStatus(Long id, StatusRequest request, String username) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Appointment not found"));

        AppUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "User not found"));

        if (user.getRole() == Role.DOCTOR && !appointment.getDoctor().getId().equals(user.getId())) {
            throw new ApiException(HttpStatus.FORBIDDEN, "Doctors can only update their own appointments");
        }

        appointment.setStatus(request.status());
        return toResponse(appointmentRepository.save(appointment));
    }

    private AppointmentResponse toResponse(Appointment a) {
        return new AppointmentResponse(
                a.getId(),
                a.getPatient().getId(),
                a.getPatient().getFullName(),
                a.getDoctor().getId(),
                a.getDoctor().getFullName(),
                a.getAppointmentDate(),
                a.getStatus()
        );
    }
}