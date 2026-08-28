package com.medilabo.risk.domain;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class TriggerDetector {

    public List<String> detect(List<String> notes) {
        String text = joinNotes(notes);
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
}
