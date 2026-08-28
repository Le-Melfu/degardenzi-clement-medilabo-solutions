package com.medilabo.risk.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.medilabo.risk.client.NoteDto;
import com.medilabo.risk.client.NotesClient;
import com.medilabo.risk.client.PatientClient;
import com.medilabo.risk.client.PatientDto;
import com.medilabo.risk.domain.RiskCalculator;
import com.medilabo.risk.domain.RiskLevel;
import com.medilabo.risk.domain.TriggerDetector;
import com.medilabo.risk.dto.RiskAssessmentResponse;

@ExtendWith(MockitoExtension.class)
class RiskAssessmentServiceTest {

    @Mock
    private NotesClient notesClient;

    @Mock
    private PatientClient patientClient;

    private RiskAssessmentService service;

    @BeforeEach
    void setUp() {
        service = new RiskAssessmentService(
                notesClient, patientClient, new TriggerDetector(), new RiskCalculator());
    }

    @Test
    void zeroTriggersSkipsPatientCall() {
        when(notesClient.findByPatId(9L)).thenReturn(List.of(note("RAS, se sent bien.")));

        RiskAssessmentResponse response = service.assess(9L);

        assertEquals(RiskLevel.NONE, response.getRisk());
        assertNull(response.getLastName());
        verify(patientClient, never()).findById(9L);
    }

    @Test
    void oneTriggerLoadsPatientAndStaysNone() {
        when(notesClient.findByPatId(1L)).thenReturn(List.of(
                note("Poids égal ou inférieur au poids recommandé.")));
        when(patientClient.findById(1L)).thenReturn(patient("Test", "TestNone", LocalDate.of(1966, 12, 31), "F"));

        RiskAssessmentResponse response = service.assess(1L);

        assertEquals(RiskLevel.NONE, response.getRisk());
        assertEquals("TestNone", response.getLastName());
        verify(patientClient).findById(1L);
    }

    private static NoteDto note(String text) {
        NoteDto dto = new NoteDto();
        dto.setNote(text);
        return dto;
    }

    private static PatientDto patient(String firstName, String lastName, LocalDate birthDate, String gender) {
        PatientDto dto = new PatientDto();
        dto.setFirstName(firstName);
        dto.setLastName(lastName);
        dto.setBirthDate(birthDate);
        dto.setGender(gender);
        return dto;
    }
}
