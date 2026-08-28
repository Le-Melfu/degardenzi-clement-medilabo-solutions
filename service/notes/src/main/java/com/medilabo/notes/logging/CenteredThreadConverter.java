package com.medilabo.notes.logging;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

/**
 * Centres the thread name in a fixed width (e.g. {@code %cthread{15}} → {@code [     main    ]}).
 */
public class CenteredThreadConverter extends ClassicConverter {

    private static final int DEFAULT_WIDTH = 15;

    @Override
    public String convert(ILoggingEvent event) {
        int width = DEFAULT_WIDTH;
        String option = getFirstOption();
        if (option != null && !option.isBlank()) {
            width = Integer.parseInt(option.trim());
        }

        String name = event.getThreadName();
        if (name.length() > width) {
            return name.substring(0, width);
        }

        int padding = width - name.length();
        int left = padding / 2;
        int right = padding - left;
        return " ".repeat(left) + name + " ".repeat(right);
    }
}
