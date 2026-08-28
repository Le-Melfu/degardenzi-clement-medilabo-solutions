package com.medilabo.risk.domain;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

@Component
public class TriggerDetector {

    private static final int NEGATION_WINDOW = 5;
    private static final Set<String> NEGATION_TOKENS = Set.of(
            "ne", "n", "pas", "sans", "aucun", "aucune", "jamais", "cesse", "arrete");

    public List<String> detect(List<String> notes, DetectionMode mode) {
        String text = joinNotes(notes);
        List<String> found = new ArrayList<>();
        for (Trigger trigger : Trigger.values()) {
            if (matches(text, trigger, mode)) {
                found.add(trigger.getLabel());
            }
        }
        return found;
    }

    private boolean matches(String text, Trigger trigger, DetectionMode mode) {
        if (found(text, trigger.getPattern(), mode)) {
            return true;
        }
        if (mode != DetectionMode.CLINICAL) {
            return false;
        }
        for (Pattern extra : trigger.getClinicalPatterns()) {
            if (found(text, extra, mode)) {
                return true;
            }
        }
        return false;
    }

    private boolean found(String text, Pattern pattern, DetectionMode mode) {
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            if (mode == DetectionMode.STRICT || !isNegated(text, matcher.start())) {
                return true;
            }
        }
        return false;
    }

    private boolean isNegated(String text, int matchStart) {
        String before = text.substring(0, matchStart).toLowerCase(Locale.ROOT);
        String[] rawTokens = before.split("[^\\p{L}]+");
        List<String> tokens = new ArrayList<>();
        for (String token : rawTokens) {
            if (!token.isBlank()) {
                tokens.add(fold(token));
            }
        }
        int from = Math.max(0, tokens.size() - NEGATION_WINDOW);
        for (int i = from; i < tokens.size(); i++) {
            if (NEGATION_TOKENS.contains(tokens.get(i))) {
                return true;
            }
        }
        return false;
    }

    private static String fold(String token) {
        String decomposed = Normalizer.normalize(token, Normalizer.Form.NFD);
        return decomposed.replaceAll("\\p{M}+", "");
    }

    private static String joinNotes(List<String> notes) {
        StringBuilder builder = new StringBuilder();
        for (String note : notes) {
            if (note == null || note.isBlank()) {
                continue;
            }
            if (!builder.isEmpty()) {
                builder.append('\n');
            }
            builder.append(note);
        }
        return builder.toString();
    }
}
