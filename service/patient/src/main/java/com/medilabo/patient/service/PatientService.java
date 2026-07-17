package com.medilabo.patient.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.medilabo.patient.dto.PatientRequest;
import com.medilabo.patient.dto.PatientResponse;
import com.medilabo.patient.model.Patient;
import com.medilabo.patient.repository.PatientRepository;

@Service
public class PatientService {

    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public List<PatientResponse> findAll() {
        return patientRepository.findAll().stream()
                .map(PatientResponse::from)
                .toList();
    }

    public PatientResponse findById(Long id) {
        return PatientResponse.from(getPatientOrThrow(id));
    }

    public PatientResponse create(PatientRequest request) {
        Patient patient = new Patient();
        applyRequest(patient, request);
        return PatientResponse.from(patientRepository.save(patient));
    }

    public PatientResponse update(Long id, PatientRequest request) {
        Patient patient = getPatientOrThrow(id);
        applyRequest(patient, request);
        return PatientResponse.from(patientRepository.save(patient));
    }

    private Patient getPatientOrThrow(Long id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Patient not found"));
    }

    private void applyRequest(Patient patient, PatientRequest request) {
        patient.setFirstName(request.getFirstName().trim());
        patient.setLastName(request.getLastName().trim());
        patient.setBirthDate(request.getBirthDate());
        patient.setGender(request.getGender().trim().toUpperCase());
        patient.setAddress(blankToNull(request.getAddress()));
        patient.setPhone(blankToNull(request.getPhone()));
    }

    private String blankToNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }
}
