package com.medilabo.notes.service;

import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Service;

import com.medilabo.notes.client.PatientClient;
import com.medilabo.notes.dto.NoteRequest;
import com.medilabo.notes.dto.NoteResponse;
import com.medilabo.notes.model.Note;
import com.medilabo.notes.repository.NoteRepository;

@Service
public class NoteService {

    private final NoteRepository noteRepository;
    private final PatientClient patientClient;

    public NoteService(NoteRepository noteRepository, PatientClient patientClient) {
        this.noteRepository = noteRepository;
        this.patientClient = patientClient;
    }

    public List<NoteResponse> findByPatId(Long patId) {
        return noteRepository.findByPatIdOrderByCreatedAtAscIdAsc(patId).stream()
                .map(NoteResponse::from)
                .toList();
    }

    public NoteResponse create(NoteRequest request) {
        patientClient.requireExists(request.getPatId());
        Note note = new Note();
        note.setPatId(request.getPatId());
        note.setNote(request.getNote());
        note.setCreatedAt(Instant.now());
        return NoteResponse.from(noteRepository.save(note));
    }
}
