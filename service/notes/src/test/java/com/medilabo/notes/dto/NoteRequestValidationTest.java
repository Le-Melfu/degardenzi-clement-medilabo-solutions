package com.medilabo.notes.dto;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

class NoteRequestValidationTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void validRequestPasses() {
        NoteRequest request = request(1L, "Note OK");
        assertTrue(validator.validate(request).isEmpty());
    }

    @Test
    void rejectsNullZeroAndNegativePatId() {
        assertFalse(validator.validate(request(null, "Note")).isEmpty());
        assertFalse(validator.validate(request(0L, "Note")).isEmpty());
        assertFalse(validator.validate(request(-1L, "Note")).isEmpty());
    }

    @Test
    void rejectsBlankNote() {
        Set<ConstraintViolation<NoteRequest>> violations = validator.validate(request(1L, "   "));
        assertFalse(violations.isEmpty());
    }

    private static NoteRequest request(Long patId, String note) {
        NoteRequest request = new NoteRequest();
        request.setPatId(patId);
        request.setNote(note);
        return request;
    }
}
