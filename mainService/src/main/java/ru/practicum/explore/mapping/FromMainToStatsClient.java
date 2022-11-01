package ru.practicum.explore.mapping;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.explore.model.Hit;

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
        return makeAndSendRequest(HttpMethod.POST, "/hit", hit);
    }

    public ResponseEntity<Object> getStats(List<String> uris, Boolean unique, String start, String end) {
        StringBuilder sb = new StringBuilder();
        for (String url : uris) {
            sb.append("uris=").append(url).append("&");
        }
        return makeAndSendRequest(HttpMethod.GET, "/stats?" + sb.toString() + "&unique=" + unique + "&start=" + start + "&end=" + end, new Hit());
    }

    private <T> ResponseEntity<Object> makeAndSendRequest(HttpMethod method, String path, T body) {
        HttpEntity<T> requestEntity = new HttpEntity<>(body);
        ResponseEntity<Object> statsResponse;
        try {
            statsResponse = rest.exchange(path, method, requestEntity, Object.class);
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
        }
        return prepareGatewayResponse(statsResponse);
    }

    private static ResponseEntity<Object> prepareGatewayResponse(ResponseEntity<Object> response) {
        if (response.getStatusCode().is2xxSuccessful()) {
            return response;
        }

        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(response.getStatusCode());

        if (response.hasBody()) {
            return responseBuilder.body(response.getBody());
        }
        ;
        return responseBuilder.build();
    }
}



