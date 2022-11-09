package ru.practicum.explore.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AdminSearchedParams {
    private List<Long> users = null;
    private List<String> states = null;
    private List<Long> categories = null;
    private LocalDateTime rangeStart;
    private LocalDateTime rangeEnd;
}
