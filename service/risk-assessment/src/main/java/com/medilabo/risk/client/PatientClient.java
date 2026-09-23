package com.medilabo.risk.client;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;

@Component
public class PatientClient {

    private final RestClient restClient;

    public PatientClient(
            @Value("${medilabo.patient.url}") String baseUrl,
            @Value("${medilabo.clients.username}") String username,
            @Value("${medilabo.clients.password}") String password) {
        this.restClient = RestClient.builder()
                .baseUrl(Objects.requireNonNull(baseUrl))
                .defaultHeaders(headers -> headers.setBasicAuth(Objects.requireNonNull(username), Objects.requireNonNull(password)))
                .build();
    }

    public PatientDto findById(Long id) {
        return restClient.get()
                .uri("/patient/{id}", id)
                .retrieve()
                .onStatus(status -> status.value() == 404, (request, response) -> {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Patient not found");
                })
                .body(PatientDto.class);
    }
}
