package ru.practicum.explore.storage;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.explore.model.Event;
import ru.practicum.explore.model.QEvent;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event>, QuerydslPredicateExecutor<QEvent> {

    Page<Event> findAll(Pageable pageable);

    Page<Event> findAllByOwnerId(long id, Pageable pageable);

    Event findEventByIdAndOwnerId(long id, long ownerId);

    List<Event> findAllByCategoryId(long catId);

    Page<Event> findAllByOwnerIdInAndStateInAndCategoryIdInAndEventDateBetween(
            List<Long> users, List<String> states, List<Long> categories,
            LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable);

    @Query(nativeQuery = true, value = "select * from events where events.paid = :pay " +
            "AND (lower(events.annotation) LIKE :text OR lower(events.description) LIKE :text) " +
            "AND  events.category IN :cat " +
            "AND events.event_date BETWEEN :start AND :end")
    Page<Event> findByParams(@Param("text") String text, @Param("cat") List<Long> categories,
                             @Param("pay") boolean paid, @Param("start") LocalDateTime rangeStart,
                             @Param("end") LocalDateTime rangeEnd, Pageable pageable);
}
