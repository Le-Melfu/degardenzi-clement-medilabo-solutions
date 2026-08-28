package com.medilabo.risk.dto;

import com.medilabo.risk.domain.RiskLevel;

public class RiskAssessmentResponse {

    private final Long patId;
    private final String firstName;
    private final String lastName;
    private final RiskLevel risk;

    public RiskAssessmentResponse(Long patId, String firstName, String lastName, RiskLevel risk) {
        this.patId = patId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.risk = risk;
    }

    public Long getPatId() {
        return patId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public RiskLevel getRisk() {
        return risk;
    }
}
