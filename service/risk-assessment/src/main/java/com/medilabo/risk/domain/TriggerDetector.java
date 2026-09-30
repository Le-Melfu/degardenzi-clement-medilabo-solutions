package com.medilabo.risk.domain;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class TriggerDetector {

    public List<String> detect(List<String> notes) {
        String text = stripAccents(joinNotes(notes));
        List<String> found = new ArrayList<>();
        for (Trigger trigger : Trigger.values()) {
            if (trigger.getPattern().matcher(text).find()) {
                found.add(trigger.getLabel());
            }
        }
        return found;
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

    /** NFD + strip combining marks so "cholestérol" and "cholesterol" both match. */
    static String stripAccents(String input) {
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        return normalized.replaceAll("\\p{M}+", "");
    }
}
