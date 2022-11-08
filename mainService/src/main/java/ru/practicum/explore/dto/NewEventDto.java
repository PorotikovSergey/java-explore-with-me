package ru.practicum.explore.dto;

import lombok.*;
import ru.practicum.explore.model.Location;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class NewEventDto {
    private Boolean paid;

    @NotNull
    private long category;

    @NotNull
    @NotBlank
    private String eventDate;

    @NotNull
    private Location location;

    private long participantLimit;
    private boolean requestModeration;

    @NotNull
    @NotBlank
    private String title;

    @NotNull
    @NotBlank
    private String annotation;

    @NotNull
    @NotBlank
    private String description;
}
