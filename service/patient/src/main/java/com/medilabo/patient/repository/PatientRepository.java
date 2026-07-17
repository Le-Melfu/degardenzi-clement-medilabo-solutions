package com.medilabo.patient.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.medilabo.patient.model.Patient;

public interface PatientRepository extends JpaRepository<Patient, Long> {
}
