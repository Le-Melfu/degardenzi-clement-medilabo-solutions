package com.medilabo.risk.domain;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class RiskCalculator {

    public RiskLevel assess(int age, Gender gender, int triggerCount) {
        // Age 30 already includes days after the birthday: treat as adult (>= 30).
        if (age >= 30) {
            return assessThirtyOrOver(triggerCount);
        }
        return assessUnderThirty(gender, triggerCount);
    }

    private static RiskLevel assessThirtyOrOver(int triggerCount) {
        if (triggerCount >= 8) {
            return RiskLevel.EARLY_ONSET;
        }
        if (triggerCount >= 6) {
            return RiskLevel.IN_DANGER;
        }
        if (triggerCount >= 2) {
            return RiskLevel.BORDERLINE;
        }
        return RiskLevel.NONE;
    }

    private static RiskLevel assessUnderThirty(Gender gender, int triggerCount) {
        if (gender == null) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Patient gender missing or invalid");
        }
        boolean male = gender == Gender.M;
        if (male) {
            if (triggerCount >= 5) {
                return RiskLevel.EARLY_ONSET;
            }
            if (triggerCount >= 3) {
                return RiskLevel.IN_DANGER;
            }
            return RiskLevel.NONE;
        }
        if (triggerCount >= 7) {
            return RiskLevel.EARLY_ONSET;
        }
        if (triggerCount >= 4) {
            return RiskLevel.IN_DANGER;
        }
        return RiskLevel.NONE;
    }
}
