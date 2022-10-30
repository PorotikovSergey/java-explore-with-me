package ru.practicum.explore;

import ru.practicum.explore.model.EndpointHit;
import ru.practicum.explore.model.Hit;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Mapper {
    private static final DateTimeFormatter FORMATTER1 = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS");
    private static final DateTimeFormatter FORMATTER2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static Hit fromEndpointHitToHit(EndpointHit endpointHit) {

        Hit hit = new Hit();
        hit.setApp(endpointHit.getApp());
        hit.setUri(endpointHit.getUri());
        hit.setIp(endpointHit.getIp());

        try {
            hit.setTimestamp(LocalDateTime.parse(endpointHit.getTimestamp(), FORMATTER1));
        } catch (DateTimeException e) {
            hit.setTimestamp(LocalDateTime.parse(endpointHit.getTimestamp(), FORMATTER2));
        }

        return hit;
    }
}
