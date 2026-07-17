package com.medilabo.gateway;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class GatewayApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void contextLoads() {
    }

    @Test
    void shouldRejectUnauthenticatedRequest() throws Exception {
        mockMvc.perform(get("/patient/"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldRejectInvalidCredentials() throws Exception {
        mockMvc.perform(get("/patient/").with(httpBasic("doctor", "wrong")))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldPassAuthenticationWithValidCredentials() throws Exception {
        // Auth OK — le statut downstream peut varier (patient off) ; on vérifie juste que ce n'est plus 401
        mockMvc.perform(get("/patient/").with(httpBasic("doctor", "password")))
                .andExpect(status().is(org.hamcrest.Matchers.not(401)));
    }

    @Test
    void shouldAuthenticateServiceAccount() throws Exception {
        mockMvc.perform(get("/patient/").with(httpBasic("service", "service")))
                .andExpect(status().is(org.hamcrest.Matchers.not(401)));
    }
}
