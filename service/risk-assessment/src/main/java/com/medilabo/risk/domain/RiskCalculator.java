package com.medilabo.risk.domain;

import org.springframework.stereotype.Component;

@Component
public class RiskCalculator {

    public RiskLevel assess(int age, String gender, int triggerCount) {
        if (triggerCount == 0) {
            return RiskLevel.NONE;
        }

        boolean adult = age >= 30;
        boolean male = gender != null && gender.equalsIgnoreCase("M");

        if (adult) {
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
