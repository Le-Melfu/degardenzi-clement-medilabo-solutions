package com.medilabo.patient.model;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Gender {
    M,
    F;

    @JsonCreator
    public static Gender from(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return Gender.valueOf(value.trim().toUpperCase());
    }
}
