package ru.practicum.explore.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

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

    @NotNull
    private long eventId;
    private Boolean paid;
    private long participantLimit;
    private String title;
}
