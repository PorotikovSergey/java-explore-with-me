package ru.practicum.explore.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CompilationDto {
    @NotNull
    private List<EventShortDto> events = new ArrayList<>();
    private long id;
    private boolean pinned;
    @NotNull
    @NotBlank
    private String title;
}
