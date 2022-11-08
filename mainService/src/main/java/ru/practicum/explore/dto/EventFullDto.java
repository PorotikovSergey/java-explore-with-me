package ru.practicum.explore.dto;

import lombok.*;
import ru.practicum.explore.model.Location;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EventFullDto {
    private Boolean paid;
    private long id;
    private CategoryDto category;
    private UserDto initiator;
    private String state;
    private Location location;
    private long participantLimit;
    private String createdOn;
    private String publishedOn;
    private String eventDate;
    private boolean requestModeration;
    private long confirmedRequests;
    private long views;

    private String title;
    private String annotation;
    private String description;
}
