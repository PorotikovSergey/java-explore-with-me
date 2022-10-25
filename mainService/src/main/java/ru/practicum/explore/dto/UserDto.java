package ru.practicum.explore.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserDto {
    private String email;
    private long id;
    private String name;
}
