package com.medilabo.notes.config;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.medilabo.notes.model.Note;
import com.medilabo.notes.repository.NoteRepository;

@Component
public class NoteDataLoader implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(NoteDataLoader.class);

    private final NoteRepository noteRepository;

    public NoteDataLoader(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    @Override
    public void run(String... args) {
        if (noteRepository.count() == 0) {
            seedAnnexeB();
        }
        backfillMissingCreatedAt();
    }

    private void seedAnnexeB() {
        noteRepository.save(note(1L, LocalDate.of(2023, 3, 15),
                "Le patient déclare qu'il « se sent très bien ». Poids égal ou inférieur au poids recommandé."));
        noteRepository.save(note(2L, LocalDate.of(2023, 5, 10),
                "Le patient déclare qu'il ressent beaucoup de stress au travail. Il se plaint également que son audition est anormale dernièrement."));
        noteRepository.save(note(2L, LocalDate.of(2023, 8, 22),
                "Le patient déclare avoir fait une réaction aux médicaments au cours des 3 derniers mois. Il remarque également que son audition continue d'être anormale."));
        noteRepository.save(note(3L, LocalDate.of(2024, 1, 12),
                "Le patient déclare qu'il fume depuis peu."));
        noteRepository.save(note(3L, LocalDate.of(2024, 6, 3),
                "Le patient déclare qu'il est fumeur et qu'il a cessé de fumer l'année dernière. Il se plaint également de crises d'apnée respiratoire anormales. Tests de laboratoire indiquant un taux de cholestérol LDL élevé."));
        noteRepository.save(note(4L, LocalDate.of(2024, 2, 8),
                "Le patient déclare qu'il lui est devenu difficile de monter les escaliers. Il se plaint également d'être essoufflé. Tests de laboratoire indiquant que les anticorps sont élevés. Réaction aux médicaments."));
        noteRepository.save(note(4L, LocalDate.of(2024, 5, 19),
                "Le patient déclare qu'il a mal au dos lorsqu'il reste assis pendant longtemps."));
        noteRepository.save(note(4L, LocalDate.of(2024, 9, 4),
                "Le patient déclare avoir commencé à fumer depuis peu. Hémoglobine A1C supérieure au niveau recommandé."));
        noteRepository.save(note(4L, LocalDate.of(2025, 1, 14),
                "Taille, Poids, Cholestérol, Vertige et Réaction."));

        log.info("Seeded 9 test notes (Annexe B)");
    }

    private void backfillMissingCreatedAt() {
        int updated = 0;
        for (Note note : noteRepository.findAll()) {
            if (note.getCreatedAt() != null) {
                continue;
            }
            Instant createdAt = Instant.now();
            if (note.getId() != null && ObjectId.isValid(note.getId())) {
                createdAt = new ObjectId(note.getId()).getDate().toInstant();
            }
            note.setCreatedAt(createdAt);
            noteRepository.save(note);
            updated++;
        }
        if (updated > 0) {
            log.info("Backfilled createdAt on {} existing notes", updated);
        }
    }

    private Note note(Long patId, LocalDate date, String text) {
        Note note = new Note();
        note.setPatId(patId);
        note.setNote(text);
        note.setCreatedAt(date.atStartOfDay().toInstant(ZoneOffset.UTC));
        return note;
    }
}
