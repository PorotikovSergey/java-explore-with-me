package ru.practicum.explore.storage;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.practicum.explore.model.Event;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event> {

    Page<Event> findAll(Pageable pageable);

    Page<Event> findAllByOwnerId(long id, Pageable pageable);

    Event findEventByIdAndOwnerId(long id, long ownerId);

    List<Event> findAllByCategoryId(long catId);

    Page<Event> findAllByOwnerIdInAndStateInAndCategoryIdInAndEventDateBetween
            (List<Long> users, List<String> states, List<Long> categories,
             LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable);

    Page<Event> findAllByAnnotationContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndCategoryIdInAndPaidAndPublishedOnNotNullAndEventDateBetween
            (String text, String text2, List<Long> categories, boolean paid,
             LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable);
}
