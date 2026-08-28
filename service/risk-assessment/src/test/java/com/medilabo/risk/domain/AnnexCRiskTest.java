package com.medilabo.risk.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class AnnexCRiskTest {

    private final TriggerDetector detector = new TriggerDetector();
    private final RiskCalculator calculator = new RiskCalculator();

    @ParameterizedTest
    @EnumSource(DetectionMode.class)
    void testNoneIsNoneInBothModes(DetectionMode mode) {
        assertEquals(RiskLevel.NONE, assess(
                LocalDate.of(1966, 12, 31),
                "F",
                List.of("Le patient déclare qu'il « se sent très bien ». Poids égal ou inférieur au poids recommandé."),
                mode));
    }

    @ParameterizedTest
    @EnumSource(DetectionMode.class)
    void testBorderlineIsBorderlineInBothModes(DetectionMode mode) {
        assertEquals(RiskLevel.BORDERLINE, assess(
                LocalDate.of(1945, 6, 24),
                "M",
                List.of(
                        "Le patient déclare qu'il ressent beaucoup de stress au travail. Il se plaint également que son audition est anormale dernièrement.",
                        "Le patient déclare avoir fait une réaction aux médicaments au cours des 3 derniers mois. Il remarque également que son audition continue d'être anormale."),
                mode));
    }

    @ParameterizedTest
    @EnumSource(DetectionMode.class)
    void testInDangerIsInDangerInBothModes(DetectionMode mode) {
        assertEquals(RiskLevel.IN_DANGER, assess(
                LocalDate.of(2004, 6, 18),
                "M",
                List.of(
                        "Le patient déclare qu'il fume depuis peu.",
                        "Le patient déclare qu'il est fumeur et qu'il a cessé de fumer l'année dernière. Il se plaint également de crises d'apnée respiratoire anormales. Tests de laboratoire indiquant un taux de cholestérol LDL élevé."),
                mode));
    }

    @ParameterizedTest
    @EnumSource(DetectionMode.class)
    void testEarlyOnsetIsEarlyOnsetInBothModes(DetectionMode mode) {
        assertEquals(RiskLevel.EARLY_ONSET, assess(
                LocalDate.of(2002, 6, 28),
                "F",
                List.of(
                        "Le patient déclare qu'il lui est devenu difficile de monter les escaliers. Il se plaint également d'être essoufflé. Tests de laboratoire indiquant que les anticorps sont élevés. Réaction aux médicaments.",
                        "Le patient déclare qu'il a mal au dos lorsqu'il reste assis pendant longtemps.",
                        "Le patient déclare avoir commencé à fumer depuis peu. Hémoglobine A1C supérieure au niveau recommandé.",
                        "Taille, Poids, Cholestérol, Vertige et Réaction."),
                mode));
    }

    private RiskLevel assess(LocalDate birthDate, String gender, List<String> notes, DetectionMode mode) {
        List<String> triggers = detector.detect(notes, mode);
        int age = Period.between(birthDate, LocalDate.now()).getYears();
        return calculator.assess(age, gender, triggers.size());
    }
}
