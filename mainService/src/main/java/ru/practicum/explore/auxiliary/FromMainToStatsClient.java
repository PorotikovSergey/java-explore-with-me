package ru.practicum.explore.auxiliary;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.util.List;

@Service
@Slf4j
public class FromMainToStatsClient {

    protected final RestTemplate rest;

    @Autowired
    public FromMainToStatsClient(@Value("${stats-service.url}") String serverUrl, RestTemplateBuilder builder) {
        this.rest = builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build();

    }

    public ResponseEntity<Object> postHit(Hit hit) {
        return postAndSendRequest(HttpMethod.POST, "/hit", hit);
    }

    public ResponseEntity<Object> getStats(List<String> uris, Boolean unique, String start, String end) {
        return getAndSendRequest(HttpMethod.GET, "/stats", uris, unique, start, end);
    }

    private <T> ResponseEntity<Object> postAndSendRequest(HttpMethod method, String path, T body) {
        HttpEntity<T> requestEntity = new HttpEntity<>(body);
        ResponseEntity<Object> statsResponse = rest.exchange(path, method, requestEntity, Object.class);
        return prepareResponse(statsResponse);
    }

    private ResponseEntity<Object> getAndSendRequest(HttpMethod method, String path, List<String> uris, Boolean unique, String start, String end) {
        ResponseEntity<Object> statsResponse = rest.exchange(path, method, new HttpEntity<>(null, null), Object.class);
        return prepareResponse(statsResponse);
    }

    private static ResponseEntity<Object> prepareResponse(ResponseEntity<Object> response) {
        if (response.getStatusCode().is2xxSuccessful()) {
            return response;
        }

        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(response.getStatusCode());

        if (response.hasBody()) {
            return responseBuilder.body(response.getBody());
        }

        return responseBuilder.build();
    }
}
