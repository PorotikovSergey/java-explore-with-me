package ru.practicum.explore.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class NewCompilationDto {
    @NotNull
    private List<Long> events;

    @NotNull
    private boolean pinned;

    @NotNull
    @NotBlank
    private String title;
}
