package ru.practicum.explore.dto;

import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class NewReview {
    @NotNull
    @NotBlank
    private String text;

    @NotNull
    @Min(1)
    @Max(10)
    private long eventRating;
}
