package ru.practicum.explore.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class NewUserRequest {
    @NotNull
    @NotBlank
    private String email;

    @NotNull
    @NotBlank
    private String name;
}
