package com.medilabo.gateway;

import static org.hamcrest.Matchers.not;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Objects;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.lang.NonNull;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

@SpringBootTest
@AutoConfigureMockMvc
class GatewayApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @NonNull
    private static RequestPostProcessor basicAuth(String username, String password) {
        return Objects.requireNonNull(httpBasic(username, password));
    }

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
        mockMvc.perform(get("/patient/").with(basicAuth("doctor", "wrong")))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldPassAuthenticationWithValidCredentials() throws Exception {
        // Auth OK — le statut downstream peut varier (patient off) ; on vérifie juste que ce n'est plus 401
        mockMvc.perform(get("/patient/").with(basicAuth("doctor", "password")))
                .andExpect(status().is(Objects.requireNonNull(not(401))));
    }
}
