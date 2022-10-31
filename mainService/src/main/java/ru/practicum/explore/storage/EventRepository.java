package ru.practicum.explore.storage;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.explore.model.Event;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    Page<Event> findAll(Pageable pageable);
    List<Event> findAllByOwnerId(long id);

    Page<Event> findAllByOwnerId(long id, Pageable pageable);

    Event findEventByIdAndOwnerId(long id, long ownerId);

    List<Event> findAllByCategoryId(long catId);

    List<Event> findAllByAnnotationContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String text, String text2);

    Page<Event> findAllByAnnotationContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String text, String text2, Pageable pageable);
}
