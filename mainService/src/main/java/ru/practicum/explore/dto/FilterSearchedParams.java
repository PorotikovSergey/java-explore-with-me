package ru.practicum.explore.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FilterSearchedParams {
    private static final String FORMATTER = "yyyy-MM-dd HH:mm:ss";
    private List<Long> categories = null;
    private Boolean paid;
    private Boolean onlyAvailable;
    private LocalDateTime rangeStart;
    private LocalDateTime rangeEnd;
    private String sort;
    private String text = null;

    public FilterSearchedParams(List<Long> categories, Boolean paid, Boolean onlyAvailable,
                                String rangeStart, String rangeEnd, String sort, String text) {
        this.categories = categories;
        this.paid = paid;
        this.onlyAvailable = onlyAvailable;
        if (rangeStart != null) {
            this.rangeStart = LocalDateTime.parse(rangeStart, DateTimeFormatter.ofPattern(FORMATTER));
        } else {
            this.rangeStart = LocalDateTime.now();
        }
        if (rangeEnd != null) {
            this.rangeEnd = LocalDateTime.parse(rangeEnd, DateTimeFormatter.ofPattern(FORMATTER));
        } else {
            this.rangeEnd = LocalDateTime.of(3000, 12, 12, 12, 12);
        }
        this.sort = sort;
        this.text = text;
    }
}
