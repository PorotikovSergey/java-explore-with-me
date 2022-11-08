package ru.practicum.explore.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.*;
import ru.practicum.explore.model.Location;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AdminUpdateEventRequest {
    private Boolean paid;

    @NotNull
    @NotBlank
    private String eventDate;

    @NotNull
    private Location location;

    private long participantLimit;

    @NotNull
    private boolean requestModeration;

    @NotNull
    private long category;

    @NotNull
    @NotBlank
    private String description;

    @NotNull
    @NotBlank
    private String annotation;

    @NotNull
    @NotBlank
    private String title;
}
