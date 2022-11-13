package ru.practicum.explore.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.explore.model.Hit;

import java.util.List;

@Repository
public interface StatsRepository extends JpaRepository<Hit, Long> {
    List<Hit> findAllByUriIn(List<String> uris);

    List<Hit> findAllByUriInAndTimestampBetween(List<String> uris, String start, String end);

    List<Hit> findDistinctByUriInAndTimestampBetween(List<String> uris, String start, String end);
}
