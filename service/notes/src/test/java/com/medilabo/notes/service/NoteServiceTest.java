package com.medilabo.notes.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.medilabo.notes.client.PatientClient;
import com.medilabo.notes.dto.NoteRequest;
import com.medilabo.notes.dto.NoteResponse;
import com.medilabo.notes.model.Note;
import com.medilabo.notes.repository.NoteRepository;

@ExtendWith(MockitoExtension.class)
class NoteServiceTest {

    @Mock
    private NoteRepository noteRepository;

    @Mock
    private PatientClient patientClient;

    private NoteService noteService;

    @BeforeEach
    void setUp() {
        noteService = new NoteService(noteRepository, patientClient);
    }

    @Test
    void createChecksPatientThenSaves() {
        NoteRequest request = new NoteRequest();
        request.setPatId(1L);
        request.setNote("Observation");
        when(noteRepository.save(any(Note.class))).thenAnswer(invocation -> {
            Note note = invocation.getArgument(0);
            note.setId("n1");
            return note;
        });

        NoteResponse response = noteService.create(request);

        verify(patientClient).requireExists(1L);
        ArgumentCaptor<Note> captor = ArgumentCaptor.forClass(Note.class);
        verify(noteRepository).save(captor.capture());
        assertEquals(1L, captor.getValue().getPatId());
        assertEquals("Observation", captor.getValue().getNote());
        assertEquals("n1", response.getId());
    }

    @Test
    void createRejectsUnknownPatient() {
        NoteRequest request = new NoteRequest();
        request.setPatId(999L);
        request.setNote("Orpheline");
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Patient not found"))
                .when(patientClient).requireExists(999L);

        ResponseStatusException error = assertThrows(
                ResponseStatusException.class, () -> noteService.create(request));

        assertEquals(HttpStatus.NOT_FOUND, error.getStatusCode());
        verify(noteRepository, never()).save(any());
    }

    @Test
    void findByPatIdMapsRepositoryResults() {
        Note note = new Note();
        note.setId("a");
        note.setPatId(2L);
        note.setNote("Texte");
        note.setCreatedAt(Instant.parse("2024-01-01T00:00:00Z"));
        when(noteRepository.findByPatIdOrderByCreatedAtAscIdAsc(2L)).thenReturn(java.util.List.of(note));

        NoteResponse response = noteService.findByPatId(2L).get(0);

        assertEquals("a", response.getId());
        assertEquals(2L, response.getPatId());
        assertEquals("Texte", response.getNote());
    }
}
