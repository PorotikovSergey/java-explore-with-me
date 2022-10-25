package ru.practicum.explore.dto;

import lombok.*;
import ru.practicum.explore.model.Location;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class NewEventDto {
    private Boolean paid;
    private long category;
    private String eventDate;
    private Location location;
    private long participantLimit;
    private boolean requestModeration;
    private String title;
    private String annotation;
    private String description;
}
