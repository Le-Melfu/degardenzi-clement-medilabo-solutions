package com.medilabo.risk.domain;

import org.springframework.stereotype.Component;

@Component
public class RiskCalculator {

    public RiskLevel assess(int age, String gender, int triggerCount) {
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

    private static RiskLevel assessUnderThirty(String gender, int triggerCount) {
        boolean male = gender != null && gender.equalsIgnoreCase("M");
        if (male) {
            if (triggerCount >= 5) {
                return RiskLevel.EARLY_ONSET;
            }
            if (triggerCount >= 3) {
                return RiskLevel.IN_DANGER;
            }
            return RiskLevel.NONE;
        } else {
            if (triggerCount >= 7) {
                return RiskLevel.EARLY_ONSET;
            }
            if (triggerCount >= 4) {
                return RiskLevel.IN_DANGER;
            }
            return RiskLevel.NONE;
        }
    }
}
