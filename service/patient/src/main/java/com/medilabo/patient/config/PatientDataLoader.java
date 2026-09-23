package com.medilabo.patient.config;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import com.medilabo.patient.model.Gender;
import com.medilabo.patient.model.Patient;
import com.medilabo.patient.repository.PatientRepository;

@Component
public class PatientDataLoader implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(PatientDataLoader.class);

    private final PatientRepository patientRepository;

    public PatientDataLoader(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Override
    public void run(String... args) {
        if (patientRepository.count() > 0) {
            return;
        }

        patientRepository.save(patient("Test", "TestNone", LocalDate.of(1966, 12, 31), Gender.F, "1 Brookside St", "100-222-3333"));
        patientRepository.save(patient("Test", "TestBorderline", LocalDate.of(1945, 6, 24), Gender.M, "2 High St", "200-333-4444"));
        patientRepository.save(patient("Test", "TestInDanger", LocalDate.of(2004, 6, 18), Gender.M, "3 Club Road", "300-444-5555"));
        patientRepository.save(patient("Test", "TestEarlyOnset", LocalDate.of(2002, 6, 28), Gender.F, "4 Valley Dr", "400-555-6666"));

        log.info("[DebugClem] Seeded 4 test patients (Annexe A)");
    }

    @NonNull
    private Patient patient(String firstName, String lastName, LocalDate birthDate, Gender gender,
            String address, String phone) {
        Patient patient = new Patient();
        patient.setFirstName(firstName);
        patient.setLastName(lastName);
        patient.setBirthDate(birthDate);
        patient.setGender(gender);
        patient.setAddress(address);
        patient.setPhone(phone);
        return patient;
    }
}
