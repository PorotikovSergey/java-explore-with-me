package ru.practicum.explore.dto;

import lombok.*;
import ru.practicum.explore.model.Location;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AdminUpdateEventRequest {
    private Boolean paid;
    private String eventDate;
    private Location location;

    private long participantLimit;
    private boolean requestModeration;
    private long category;

    private String description;
    private String annotation;
    private String title;
}
