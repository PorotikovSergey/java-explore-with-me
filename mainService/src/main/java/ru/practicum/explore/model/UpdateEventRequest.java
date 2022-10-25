package ru.practicum.explore.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UpdateEventRequest {
    private String annotation;
    private long category;
    private String description;
    private String eventDate;
    private long eventId;
    private Boolean paid;
    private long participantLimit;
    private String title;
}
