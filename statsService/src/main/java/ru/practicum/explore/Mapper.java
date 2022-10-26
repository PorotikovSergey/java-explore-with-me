package ru.practicum.explore;

import org.springframework.stereotype.Service;
import ru.practicum.explore.model.EndpointHit;
import ru.practicum.explore.model.Hit;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class Mapper {

    public Hit fromEndpointHitToHit(EndpointHit endpointHit) {
        Hit hit = new Hit();
        hit.setApp(endpointHit.getApp());
        hit.setUri(endpointHit.getUri());
        hit.setIp(endpointHit.getIp());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS");
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        try {
            hit.setTimestamp(LocalDateTime.parse(endpointHit.getTimestamp(), formatter));
        } catch (DateTimeException e) {
            hit.setTimestamp(LocalDateTime.parse(endpointHit.getTimestamp(), formatter1));
        }

        return hit;
    }
}
