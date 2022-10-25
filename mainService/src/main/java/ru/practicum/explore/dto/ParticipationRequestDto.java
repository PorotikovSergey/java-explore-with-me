package ru.practicum.explore.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ParticipationRequestDto {
    private String created;
    private long event;
    private long id;
    private long requester;
    private String status;
}
