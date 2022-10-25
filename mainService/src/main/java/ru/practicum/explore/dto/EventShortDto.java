package ru.practicum.explore.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EventShortDto {
    private Boolean paid;
    private long id;
    private CategoryDto category;
    private long confirmedRequests;
    private String eventDate;
    private long views;
    private UserShortDto initiator;

    private String title;
    private String annotation;

}
