package com.medilabo.risk.domain;

import com.fasterxml.jackson.annotation.JsonValue;

public enum DetectionMode {
    CLINICAL("clinical"),
    STRICT("strict");

    private final String label;

    DetectionMode(String label) {
        this.label = label;
    }

    @JsonValue
    public String getLabel() {
        return label;
    }

    public static DetectionMode fromParam(String mode) {
        if (mode != null && mode.equalsIgnoreCase("strict")) {
            return STRICT;
        }
        return CLINICAL;
    }
}
