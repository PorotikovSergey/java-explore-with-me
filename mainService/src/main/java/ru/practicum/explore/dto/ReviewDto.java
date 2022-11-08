package ru.practicum.explore.dto;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ReviewDto {
    private long id;

    private String authorName;

    private String eventTitle;

    private String text;

    private long eventRating;

    private long commentRating;
}
