package com.medilabo.risk.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.medilabo.risk.domain.DetectionMode;
import com.medilabo.risk.domain.RiskLevel;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RiskAssessmentResponse {

    private Long patId;
    private String firstName;
    private String lastName;
    private Integer age;
    private String gender;
    private RiskLevel risk;
    private int triggerCount;
    private List<String> triggers;
    private DetectionMode mode;

    public static RiskAssessmentResponse withoutPatient(
            Long patId, RiskLevel risk, List<String> triggers, DetectionMode mode) {
        RiskAssessmentResponse response = new RiskAssessmentResponse();
        response.patId = patId;
        response.risk = risk;
        response.triggerCount = triggers.size();
        response.triggers = triggers;
        response.mode = mode;
        return response;
    }

    public Long getPatId() {
        return patId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public RiskLevel getRisk() {
        return risk;
    }

    public void setRisk(RiskLevel risk) {
        this.risk = risk;
    }

    public int getTriggerCount() {
        return triggerCount;
    }

    public void setTriggerCount(int triggerCount) {
        this.triggerCount = triggerCount;
    }

    public List<String> getTriggers() {
        return triggers;
    }

    public void setTriggers(List<String> triggers) {
        this.triggers = triggers;
    }

    public DetectionMode getMode() {
        return mode;
    }
}
