package ru.practicum.explore.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EndpointHit {

    private String app;

    private String uri;
    private String ip;

    private String timestamp;
}
