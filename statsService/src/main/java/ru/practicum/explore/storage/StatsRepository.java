package ru.practicum.explore.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.explore.model.Hit;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatsRepository extends JpaRepository<Hit, Long> {
    List<Hit> findAllByUriIn(List<String> uris);

    List<Hit> findAllByUriInAndAndTimestampBetween(List<String> uris, LocalDateTime start, LocalDateTime end);
}
