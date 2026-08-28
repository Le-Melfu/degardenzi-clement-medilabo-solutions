package com.medilabo.risk;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Objects;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.lang.NonNull;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import com.medilabo.risk.client.NotesClient;
import com.medilabo.risk.client.PatientClient;

@SpringBootTest
@AutoConfigureMockMvc
class RiskAssessmentApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private NotesClient notesClient;

    @MockitoBean
    private PatientClient patientClient;

    @NonNull
    private static RequestPostProcessor doctorAuth() {
        return Objects.requireNonNull(httpBasic("doctor", "password"));
    }

    @Test
    void contextLoads() {
    }

    @Test
    void shouldRejectUnauthenticatedRequest() throws Exception {
        mockMvc.perform(get("/risk/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void emptyNotesReturnNoneWithoutPatient() throws Exception {
        Mockito.when(notesClient.findByPatId(1L)).thenReturn(List.of());

        mockMvc.perform(get("/risk/1").with(doctorAuth()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.patId").value(1))
                .andExpect(jsonPath("$.risk").value("None"));

        Mockito.verifyNoInteractions(patientClient);
    }
}
