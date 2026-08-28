package com.medilabo.risk.service;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.medilabo.risk.client.NoteDto;
import com.medilabo.risk.client.NotesClient;
import com.medilabo.risk.client.PatientClient;
import com.medilabo.risk.client.PatientDto;
import com.medilabo.risk.domain.RiskCalculator;
import com.medilabo.risk.domain.RiskLevel;
import com.medilabo.risk.domain.TriggerDetector;
import com.medilabo.risk.dto.RiskAssessmentResponse;

@Service
public class RiskAssessmentService {

    private final NotesClient notesClient;
    private final PatientClient patientClient;
    private final TriggerDetector triggerDetector;
    private final RiskCalculator riskCalculator;

    public RiskAssessmentService(
            NotesClient notesClient,
            PatientClient patientClient,
            TriggerDetector triggerDetector,
            RiskCalculator riskCalculator) {
        this.notesClient = notesClient;
        this.patientClient = patientClient;
        this.triggerDetector = triggerDetector;
        this.riskCalculator = riskCalculator;
    }

    public RiskAssessmentResponse assess(Long patId) {
        List<String> notes = noteTexts(notesClient.findByPatId(patId));
        PatientDto patient = patientClient.findById(patId);
        int age = Period.between(patient.getBirthDate(), LocalDate.now()).getYears();
        List<String> triggers = triggerDetector.detect(notes);
        RiskLevel risk = riskCalculator.assess(age, patient.getGender(), triggers.size());
        return new RiskAssessmentResponse(patId, patient.getFirstName(), patient.getLastName(), risk);
    }

    private static List<String> noteTexts(List<NoteDto> notes) {
        List<String> texts = new ArrayList<>();
        for (NoteDto note : notes) {
            if (note.getNote() != null && !note.getNote().isBlank()) {
                texts.add(note.getNote());
            }
        }
        return texts;
    }
}
