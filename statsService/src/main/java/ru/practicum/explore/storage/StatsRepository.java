package ru.practicum.explore.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.explore.model.EndpointHit;

import java.util.List;

@Repository
public interface StatsRepository extends JpaRepository<EndpointHit, Long> {
    List<EndpointHit> findAllByUriIn(List<String> uris);
}
