package com.medilabo.notes;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Instant;

import org.junit.jupiter.api.Test;

import com.medilabo.notes.dto.NoteResponse;
import com.medilabo.notes.model.Note;

class NotesApplicationTests {

    @Test
    void noteResponseMapsFields() {
        Instant createdAt = Instant.parse("2024-05-19T00:00:00Z");
        Note note = new Note();
        note.setId("abc");
        note.setPatId(1L);
        note.setNote("Texte libre");
        note.setCreatedAt(createdAt);

        NoteResponse response = NoteResponse.from(note);

        assertEquals("abc", response.getId());
        assertEquals(1L, response.getPatId());
        assertEquals("Texte libre", response.getNote());
        assertEquals(createdAt, response.getCreatedAt());
    }
}
