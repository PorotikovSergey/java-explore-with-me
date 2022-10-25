package ru.practicum.explore.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilterSearchedParams {
    private List<Long> categories;
    private Boolean paid;
    private Boolean onlyAvailable;
    private String rangeStart;
    private String rangeEnd;
    private String sort;
    private String text;
}
