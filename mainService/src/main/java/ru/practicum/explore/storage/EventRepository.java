package ru.practicum.explore.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.explore.model.Event;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findAllByOwnerId(long id);

    Event findEventByIdAndOwnerId(long id, long ownerId);

    List<Event> findAllByCategoryId(long catId);

    List<Event> findAllByAnnotationContainingIgnoreCase(String text);

    List<Event> findAllByDescriptionContainingIgnoreCase(String text);
}
