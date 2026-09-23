package com.medilabo.patient.dto;

import java.time.LocalDate;

import com.medilabo.patient.model.Gender;
import com.medilabo.patient.model.Patient;

public class PatientResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private Gender gender;
    private String address;
    private String phone;

    public static PatientResponse from(Patient patient) {
        PatientResponse response = new PatientResponse();
        response.id = patient.getId();
        response.firstName = patient.getFirstName();
        response.lastName = patient.getLastName();
        response.birthDate = patient.getBirthDate();
        response.gender = patient.getGender();
        response.address = patient.getAddress();
        response.phone = patient.getPhone();
        return response;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public Gender getGender() {
        return gender;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }
}
