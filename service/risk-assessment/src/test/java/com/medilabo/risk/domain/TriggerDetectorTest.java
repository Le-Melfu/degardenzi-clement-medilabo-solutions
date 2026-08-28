package com.medilabo.risk.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

class TriggerDetectorTest {

    private final TriggerDetector detector = new TriggerDetector();

    @Test
    void annexBTestNoneHasPoidsOnly() {
        List<String> found = detector.detect(List.of(
                "Le patient déclare qu'il « se sent très bien ». Poids égal ou inférieur au poids recommandé."),
                DetectionMode.CLINICAL);
        assertEquals(List.of("Poids"), found);
    }

    @Test
    void annexBTestBorderlineHasAnormalAndReactionOnceEach() {
        List<String> found = detector.detect(List.of(
                "Le patient déclare qu'il ressent beaucoup de stress au travail. Il se plaint également que son audition est anormale dernièrement.",
                "Le patient déclare avoir fait une réaction aux médicaments au cours des 3 derniers mois. Il remarque également que son audition continue d'être anormale."),
                DetectionMode.STRICT);
        assertEquals(List.of("Anormal", "Réaction"), found);
    }

    @Test
    void annexBTestInDangerKeepsFumeurWhenAnotherMentionIsNotNegated() {
        List<String> found = detector.detect(List.of(
                "Le patient déclare qu'il fume depuis peu.",
                "Le patient déclare qu'il est fumeur et qu'il a cessé de fumer l'année dernière. Il se plaint également de crises d'apnée respiratoire anormales. Tests de laboratoire indiquant un taux de cholestérol LDL élevé."),
                DetectionMode.CLINICAL);
        assertEquals(List.of("Fumeur", "Anormal", "Cholestérol"), found);
    }

    @Test
    void annexBTestEarlyOnsetFindsEightDistinctTriggers() {
        List<String> found = detector.detect(List.of(
                "Le patient déclare qu'il lui est devenu difficile de monter les escaliers. Il se plaint également d'être essoufflé. Tests de laboratoire indiquant que les anticorps sont élevés. Réaction aux médicaments.",
                "Le patient déclare qu'il a mal au dos lorsqu'il reste assis pendant longtemps.",
                "Le patient déclare avoir commencé à fumer depuis peu. Hémoglobine A1C supérieure au niveau recommandé.",
                "Taille, Poids, Cholestérol, Vertige et Réaction."),
                DetectionMode.STRICT);
        assertEquals(List.of(
                "Hémoglobine A1C",
                "Taille",
                "Poids",
                "Fumeur",
                "Cholestérol",
                "Vertiges",
                "Réaction",
                "Anticorps"), found);
        assertEquals(8, found.size());
    }

    @Test
    void clinicalIgnoresNegatedFumeWhileStrictStillCountsIt() {
        List<String> notes = List.of("Le patient ne fume pas.");
        List<String> clinical = detector.detect(notes, DetectionMode.CLINICAL);
        List<String> strict = detector.detect(notes, DetectionMode.STRICT);
        assertFalse(clinical.contains("Fumeur"));
        assertTrue(strict.contains("Fumeur"));
    }

    @Test
    void wordBoundaryDoesNotMatchParfum() {
        List<String> found = detector.detect(List.of("Le patient aime le parfum."), DetectionMode.STRICT);
        assertFalse(found.contains("Fumeur"));
    }

    @Test
    void clinicalSynonymsForVertigesAreIgnoredInStrict() {
        List<String> notes = List.of("Le patient décrit des pertes d'équilibre et une désorientation.");
        List<String> clinical = detector.detect(notes, DetectionMode.CLINICAL);
        List<String> strict = detector.detect(notes, DetectionMode.STRICT);
        assertTrue(clinical.contains("Vertiges"));
        assertEquals(1, clinical.size());
        assertFalse(strict.contains("Vertiges"));
    }
}
