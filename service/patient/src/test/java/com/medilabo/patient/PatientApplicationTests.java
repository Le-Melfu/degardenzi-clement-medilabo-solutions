package com.medilabo.patient;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class PatientApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void contextLoads() {
    }

    @Test
    void shouldRejectUnauthenticatedRequest() throws Exception {
        mockMvc.perform(get("/patient"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldListSeededPatients() throws Exception {
        mockMvc.perform(get("/patient").with(httpBasic("doctor", "password")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", greaterThanOrEqualTo(4)))
                .andExpect(jsonPath("$[*].lastName", hasItem("TestNone")));
    }

    @Test
    void shouldGetPatientById() throws Exception {
        mockMvc.perform(get("/patient/1").with(httpBasic("doctor", "password")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastName").value("TestNone"))
                .andExpect(jsonPath("$.firstName").value("Test"));
    }

    @Test
    void shouldCreatePatient() throws Exception {
        String body = """
                {
                  "firstName": "Alice",
                  "lastName": "Martin",
                  "birthDate": "1990-05-10",
                  "gender": "F",
                  "address": "10 Rue Test",
                  "phone": "555-0000"
                }
                """;

        mockMvc.perform(post("/patient")
                        .with(httpBasic("doctor", "password"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.lastName").value("Martin"));
    }

    @Test
    void shouldUpdatePatient() throws Exception {
        String body = """
                {
                  "firstName": "Test",
                  "lastName": "TestNone",
                  "birthDate": "1966-12-31",
                  "gender": "F",
                  "address": "1 Brookside St Updated",
                  "phone": "100-222-3333"
                }
                """;

        mockMvc.perform(put("/patient/1")
                        .with(httpBasic("doctor", "password"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.address").value("1 Brookside St Updated"));
    }

    @Test
    void shouldReturnNotFoundForUnknownPatient() throws Exception {
        mockMvc.perform(get("/patient/9999").with(httpBasic("doctor", "password")))
                .andExpect(status().isNotFound());
    }
}
