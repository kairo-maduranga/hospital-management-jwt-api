package com.example.hospital_manage.service;

import com.example.hospital_manage.exception.ApiException;
import com.example.hospital_manage.model.Patient;
import com.example.hospital_manage.repository.AppointmentRepository;
import com.example.hospital_manage.repository.MedicalRecordRepository;
import com.example.hospital_manage.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;
    private final MedicalRecordRepository medicalRecordRepository;

    public Patient createPatient(Patient patient) {
        return patientRepository.save(patient);
    }

    public List<Patient> getAllPatients(String search) {
        if (search != null && !search.isBlank()) {
            return patientRepository.findByFullNameContainingIgnoreCase(search);
        }
        return patientRepository.findAll();
    }

    public Patient getPatientById(Long id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Patient not found"));
    }

    public void deletePatient(Long id) {
        Patient patient = getPatientById(id);
        if (appointmentRepository.existsByPatientId(id) || medicalRecordRepository.existsByPatientId(id)) {
            throw new ApiException(HttpStatus.CONFLICT, "Cannot delete patient with existing appointments or medical records");
        }
        patientRepository.delete(patient);
    }
}