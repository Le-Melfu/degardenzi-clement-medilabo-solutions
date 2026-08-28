package com.medilabo.risk.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class RiskCalculatorTest {

    private final RiskCalculator calculator = new RiskCalculator();

    @Test
    void zeroTriggersReturnsNoneWithoutUsingAgeOrGender() {
        assertEquals(RiskLevel.NONE, calculator.assess(12, "M", 0));
        assertEquals(RiskLevel.NONE, calculator.assess(80, "F", 0));
    }

    @Test
    void oneTriggerIsNoneForAdultsAndMinors() {
        assertEquals(RiskLevel.NONE, calculator.assess(59, "F", 1));
        assertEquals(RiskLevel.NONE, calculator.assess(22, "M", 1));
        assertEquals(RiskLevel.NONE, calculator.assess(24, "F", 1));
    }

    @Test
    void adultBorderlineInDangerAndEarlyOnset() {
        assertEquals(RiskLevel.BORDERLINE, calculator.assess(45, "M", 2));
        assertEquals(RiskLevel.BORDERLINE, calculator.assess(45, "F", 5));
        assertEquals(RiskLevel.IN_DANGER, calculator.assess(45, "M", 6));
        assertEquals(RiskLevel.IN_DANGER, calculator.assess(45, "F", 7));
        assertEquals(RiskLevel.EARLY_ONSET, calculator.assess(45, "M", 8));
        assertEquals(RiskLevel.EARLY_ONSET, calculator.assess(45, "M", 12));
    }

    @Test
    void ageThirtyUsesAdultBranch() {
        assertEquals(RiskLevel.BORDERLINE, calculator.assess(30, "M", 2));
        assertEquals(RiskLevel.IN_DANGER, calculator.assess(30, "F", 6));
    }

    @Test
    void youngMaleThresholds() {
        assertEquals(RiskLevel.NONE, calculator.assess(22, "M", 2));
        assertEquals(RiskLevel.IN_DANGER, calculator.assess(22, "M", 3));
        assertEquals(RiskLevel.IN_DANGER, calculator.assess(22, "M", 4));
        assertEquals(RiskLevel.EARLY_ONSET, calculator.assess(22, "M", 5));
    }

    @Test
    void youngFemaleThresholds() {
        assertEquals(RiskLevel.NONE, calculator.assess(24, "F", 3));
        assertEquals(RiskLevel.IN_DANGER, calculator.assess(24, "F", 4));
        assertEquals(RiskLevel.IN_DANGER, calculator.assess(24, "F", 6));
        assertEquals(RiskLevel.EARLY_ONSET, calculator.assess(24, "F", 7));
    }
}
