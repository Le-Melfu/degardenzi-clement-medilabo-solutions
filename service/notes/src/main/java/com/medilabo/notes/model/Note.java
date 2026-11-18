package com.medilabo.notes.model;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

// Serves findByPatIdOrderByCreatedAtAscIdAsc: filter on patId, already sorted by createdAt.
@Document(collection = "notes")
@CompoundIndex(name = "patId_createdAt", def = "{'patId': 1, 'createdAt': 1}")
public class Note {

    @Id
    private String id;

    private Long patId;

    private String note;

    private Instant createdAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getPatId() {
        return patId;
    }

    public void setPatId(Long patId) {
        this.patId = patId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
