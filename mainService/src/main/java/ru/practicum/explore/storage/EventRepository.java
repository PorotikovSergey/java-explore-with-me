package ru.practicum.explore.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.explore.model.Event;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    public List<Event> findAllByOwnerId(long id);

    public Event findEventByIdAndOwnerId(long id, long ownerId);

    public List<Event> findAllByOwnerIdInAndAndCategoryIdIn(List<Long> userIds, List<Long> categoryIds);

    public List<Event> findAllByCategoryId(long catId);

    public List<Event> findAllByAnnotationContainingIgnoreCase(String text);

    public List<Event> findAllByDescriptionContainingIgnoreCase(String text);
}
