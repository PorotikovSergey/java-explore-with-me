package ru.practicum.explore.apierrors;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ApiError {
    private String message;
    private String reason;
    private String status;
    private String timestamp;
}
