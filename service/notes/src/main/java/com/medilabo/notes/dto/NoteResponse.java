package com.medilabo.notes.dto;

import java.time.Instant;

import com.medilabo.notes.model.Note;

public class NoteResponse {

    private String id;
    private Long patId;
    private String note;
    private Instant createdAt;

    public static NoteResponse from(Note note) {
        NoteResponse response = new NoteResponse();
        response.id = note.getId();
        response.patId = note.getPatId();
        response.note = note.getNote();
        response.createdAt = note.getCreatedAt();
        return response;
    }

    public String getId() {
        return id;
    }

    public Long getPatId() {
        return patId;
    }

    public String getNote() {
        return note;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
