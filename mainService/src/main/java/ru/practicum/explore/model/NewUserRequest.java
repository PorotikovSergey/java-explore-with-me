package ru.practicum.explore.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class NewUserRequest {
    private String email;
    private String name;
}
