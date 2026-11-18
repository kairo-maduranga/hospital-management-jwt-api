package com.example.hospital_manage.service;

import com.example.hospital_manage.dto.MedicalRecordRequest;
import com.example.hospital_manage.dto.MedicalRecordResponse;
import com.example.hospital_manage.exception.ApiException;
import com.example.hospital_manage.model.AppUser;
import com.example.hospital_manage.model.MedicalRecord;
import com.example.hospital_manage.model.Patient;
import com.example.hospital_manage.repository.MedicalRecordRepository;
import com.example.hospital_manage.repository.PatientRepository;
import com.example.hospital_manage.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicalRecordService {

    private final MedicalRecordRepository medicalRecordRepository;
    private final PatientRepository patientRepository;
    private final UserRepository userRepository;

    public MedicalRecordResponse createRecord(MedicalRecordRequest request, String doctorUsername) {
        Patient patient = patientRepository.findById(request.patientId())
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Patient not found"));

        AppUser doctor = userRepository.findByUsername(doctorUsername)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Doctor not found"));

        MedicalRecord record = MedicalRecord.builder()
                .patient(patient)
                .doctor(doctor)
                .diagnosis(request.diagnosis())
                .prescription(request.prescription())
                .createdAt(LocalDateTime.now())
                .build();

        return toResponse(medicalRecordRepository.save(record));
    }

    public List<MedicalRecordResponse> getRecordsByPatient(Long patientId) {
        return medicalRecordRepository.findByPatientId(patientId).stream()
                .map(this::toResponse)
                .toList();
    }

    private MedicalRecordResponse toResponse(MedicalRecord r) {
        return new MedicalRecordResponse(
                r.getId(),
                r.getPatient().getId(),
                r.getPatient().getFullName(),
                r.getDoctor().getId(),
                r.getDoctor().getFullName(),
                r.getDiagnosis(),
                r.getPrescription(),
                r.getCreatedAt()
        );
    }
}