package com.medilabo.risk.domain;

import java.util.regex.Pattern;

public enum Trigger {
    HEMOGLOBINE_A1C("Hémoglobine A1C", "\\bhemoglobine\\s+a1c\\b"),
    MICROALBUMINE("Microalbumine", "\\bmicroalbumine\\b"),
    TAILLE("Taille", "\\btaille\\b"),
    POIDS("Poids", "\\bpoids\\b"),
    FUMEUR("Fumeur", "\\bfum(?:euse|eur|er|e)s?\\b"),
    ANORMAL("Anormal", "\\banormal(?:e|es|aux)?\\b"),
    CHOLESTEROL("Cholestérol", "\\bcholesterol\\b"),
    VERTIGES("Vertiges", "\\bvertiges?\\b"),
    RECHUTE("Rechute", "\\brechute\\b"),
    REACTION("Réaction", "\\breaction\\b"),
    ANTICORPS("Anticorps", "\\banticorps\\b");

    private static final int FLAGS =
            Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE | Pattern.UNICODE_CHARACTER_CLASS;

    private final String label;
    private final Pattern pattern;

    Trigger(String label, String regex) {
        this.label = label;
        this.pattern = Pattern.compile(regex, FLAGS);
    }

    public String getLabel() {
        return label;
    }

    public Pattern getPattern() {
        return pattern;
    }
}
