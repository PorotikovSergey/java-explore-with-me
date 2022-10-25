package ru.practicum.explore.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class NewCompilationDto {
    private List<Long> events;
    private boolean pinned;
    private String title;
}
