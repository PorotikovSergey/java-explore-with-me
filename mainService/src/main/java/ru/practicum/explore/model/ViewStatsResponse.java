package ru.practicum.explore.model;

import lombok.*;

import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
public class ViewStatsResponse {
    private String app;
    private String uri;

    private long hits;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ViewStatsResponse viewStats = (ViewStatsResponse) o;
        return Objects.equals(app, viewStats.app) && Objects.equals(uri, viewStats.uri);
    }

    @Override
    public int hashCode() {
        return Objects.hash(app, uri);
    }
}
