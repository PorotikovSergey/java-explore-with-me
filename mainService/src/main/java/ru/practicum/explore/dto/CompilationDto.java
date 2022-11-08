package ru.practicum.explore.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CompilationDto {
    private List<EventShortDto> events = new ArrayList<>();
    private long id;
    private boolean pinned;
    private String title;
}
