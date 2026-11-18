package com.example.hospital_manage.config;

import com.example.hospital_manage.model.AppUser;
import com.example.hospital_manage.model.Patient;
import com.example.hospital_manage.model.Role;
import com.example.hospital_manage.repository.PatientRepository;
import com.example.hospital_manage.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            userRepository.save(AppUser.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin123"))
                    .fullName("System Administrator")
                    .role(Role.ADMIN)
                    .build());

            userRepository.save(AppUser.builder()
                    .username("dr_silva")
                    .password(passwordEncoder.encode("doc123"))
                    .fullName("Dr. Kamal Silva")
                    .role(Role.DOCTOR)
                    .build());
        }

        if (patientRepository.count() == 0) {
            patientRepository.save(Patient.builder()
                    .fullName("Nimal Perera")
                    .phone("0771234567")
                    .age(35)
                    .gender("Male")
                    .address("Colombo")
                    .build());
        }
    }
}