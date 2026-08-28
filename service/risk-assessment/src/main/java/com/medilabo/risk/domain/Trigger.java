package com.medilabo.risk.domain;

import java.util.regex.Pattern;

public enum Trigger {
    HEMOGLOBINE_A1C("Hémoglobine A1C", "\\bhémoglobine\\s+a1c\\b"),
    MICROALBUMINE("Microalbumine", "\\bmicroalbumine\\b"),
    TAILLE("Taille", "\\btaille\\b"),
    POIDS("Poids", "\\bpoids\\b"),
    FUMEUR("Fumeur", "\\bfum(?:euse|eur|er|e)s?\\b"),
    ANORMAL("Anormal", "\\banormal(?:e|es|aux)?\\b"),
    CHOLESTEROL("Cholestérol", "\\bcholestérol\\b"),
    VERTIGES("Vertiges", "\\bvertiges?\\b",
            "\\bpertes?\\s+d['’]équilibres?\\b",
            "\\bdésorientations?\\b"),
    RECHUTE("Rechute", "\\brechute\\b"),
    REACTION("Réaction", "\\bréaction\\b"),
    ANTICORPS("Anticorps", "\\banticorps\\b");

    private static final int FLAGS =
            Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE | Pattern.UNICODE_CHARACTER_CLASS;

    private final String label;
    private final Pattern pattern;
    private final Pattern[] clinicalPatterns;

    Trigger(String label, String regex, String... clinicalRegexes) {
        this.label = label;
        this.pattern = Pattern.compile(regex, FLAGS);
        this.clinicalPatterns = new Pattern[clinicalRegexes.length];
        for (int i = 0; i < clinicalRegexes.length; i++) {
            this.clinicalPatterns[i] = Pattern.compile(clinicalRegexes[i], FLAGS);
        }
    }

    public String getLabel() {
        return label;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public Pattern[] getClinicalPatterns() {
        return clinicalPatterns;
    }
}
