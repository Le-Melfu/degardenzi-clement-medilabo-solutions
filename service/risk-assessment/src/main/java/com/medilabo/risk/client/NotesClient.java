package com.medilabo.risk.client;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class NotesClient {

    private final RestClient restClient;

    public NotesClient(
            @Value("${medilabo.notes.url}") String baseUrl,
            @Value("${medilabo.clients.username}") String username,
            @Value("${medilabo.clients.password}") String password) {
        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .defaultHeaders(headers -> headers.setBasicAuth(username, password))
                .build();
    }

    public List<NoteDto> findByPatId(Long patId) {
        List<NoteDto> notes = restClient.get()
                .uri("/notes/patient/{patId}", patId)
                .retrieve()
                .body(new ParameterizedTypeReference<List<NoteDto>>() {});
        return notes == null ? List.of() : notes;
    }
}
