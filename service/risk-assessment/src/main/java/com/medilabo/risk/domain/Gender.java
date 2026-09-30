package com.medilabo.risk.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

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

    @JsonValue
    public String toJson() {
        return name();
    }
}
