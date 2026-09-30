package com.medilabo.notes.client;

import java.time.Duration;
import java.util.Objects;

import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

final class UpstreamRestClient {

    private static final Duration CONNECT_TIMEOUT = Duration.ofSeconds(2);
    private static final Duration READ_TIMEOUT = Duration.ofSeconds(3);

    private UpstreamRestClient() {
    }

    static RestClient create(String baseUrl, String username, String password) {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(CONNECT_TIMEOUT);
        requestFactory.setReadTimeout(READ_TIMEOUT);
        return RestClient.builder()
                .requestFactory(requestFactory)
                .baseUrl(Objects.requireNonNull(baseUrl))
                .defaultHeaders(headers -> headers.setBasicAuth(
                        Objects.requireNonNull(username), Objects.requireNonNull(password)))
                .build();
    }
}
