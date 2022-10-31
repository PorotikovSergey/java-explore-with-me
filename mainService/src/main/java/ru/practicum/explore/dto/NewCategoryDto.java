package ru.practicum.explore.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class NewCategoryDto {
    private long id;

    @NotNull
    @NotBlank
    private String name;
}
