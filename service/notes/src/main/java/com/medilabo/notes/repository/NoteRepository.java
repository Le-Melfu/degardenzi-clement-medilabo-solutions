package com.medilabo.notes.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.medilabo.notes.model.Note;

public interface NoteRepository extends MongoRepository<Note, String> {

    List<Note> findByPatIdOrderByCreatedAtAscIdAsc(Long patId);
}
