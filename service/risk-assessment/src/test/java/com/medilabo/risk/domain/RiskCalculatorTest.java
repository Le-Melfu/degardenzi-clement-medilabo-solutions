package com.medilabo.risk.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class RiskCalculatorTest {

    private final RiskCalculator calculator = new RiskCalculator();

    @Test
    void zeroTriggersReturnsNoneWithoutUsingAgeOrGender() {
        assertEquals(RiskLevel.NONE, calculator.assess(12, Gender.M, 0));
        assertEquals(RiskLevel.NONE, calculator.assess(80, Gender.F, 0));
    }

    @Test
    void oneTriggerIsNoneForAdultsAndMinors() {
        assertEquals(RiskLevel.NONE, calculator.assess(59, Gender.F, 1));
        assertEquals(RiskLevel.NONE, calculator.assess(22, Gender.M, 1));
        assertEquals(RiskLevel.NONE, calculator.assess(24, Gender.F, 1));
    }

    @Test
    void adultBorderlineInDangerAndEarlyOnset() {
        assertEquals(RiskLevel.BORDERLINE, calculator.assess(45, Gender.M, 2));
        assertEquals(RiskLevel.BORDERLINE, calculator.assess(45, Gender.F, 5));
        assertEquals(RiskLevel.IN_DANGER, calculator.assess(45, Gender.M, 6));
        assertEquals(RiskLevel.IN_DANGER, calculator.assess(45, Gender.F, 7));
        assertEquals(RiskLevel.EARLY_ONSET, calculator.assess(45, Gender.M, 8));
        assertEquals(RiskLevel.EARLY_ONSET, calculator.assess(45, Gender.M, 12));
    }

    @Test
    void ageThirtyUsesAdultBranch() {
        assertEquals(RiskLevel.BORDERLINE, calculator.assess(30, Gender.M, 2));
        assertEquals(RiskLevel.IN_DANGER, calculator.assess(30, Gender.F, 6));
    }

    @Test
    void youngMaleThresholds() {
        assertEquals(RiskLevel.NONE, calculator.assess(22, Gender.M, 2));
        assertEquals(RiskLevel.IN_DANGER, calculator.assess(22, Gender.M, 3));
        assertEquals(RiskLevel.IN_DANGER, calculator.assess(22, Gender.M, 4));
        assertEquals(RiskLevel.EARLY_ONSET, calculator.assess(22, Gender.M, 5));
    }

    @Test
    void youngFemaleThresholds() {
        assertEquals(RiskLevel.NONE, calculator.assess(24, Gender.F, 3));
        assertEquals(RiskLevel.IN_DANGER, calculator.assess(24, Gender.F, 4));
        assertEquals(RiskLevel.IN_DANGER, calculator.assess(24, Gender.F, 6));
        assertEquals(RiskLevel.EARLY_ONSET, calculator.assess(24, Gender.F, 7));
    }
}
