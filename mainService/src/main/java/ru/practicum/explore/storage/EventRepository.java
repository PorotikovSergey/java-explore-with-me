package ru.practicum.explore.storage;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore.dto.FilterSearchedParams;
import ru.practicum.explore.model.Event;
import ru.practicum.explore.model.QEvent;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event> , QuerydslPredicateExecutor<QEvent> {

    Page<Event> findAll(Pageable pageable);

    Page<Event> findAllByOwnerId(long id, Pageable pageable);

    Event findEventByIdAndOwnerId(long id, long ownerId);

    List<Event> findAllByCategoryId(long catId);

    Page<Event> findAllByOwnerIdInAndStateInAndCategoryIdInAndEventDateBetween(
            List<Long> users, List<String> states, List<Long> categories,
            LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable);

    Page<Event> findAllByAnnotationContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndCategoryIdInAndPaidAndPublishedOnNotNullAndEventDateBetweenOrderByEventDateAsc(
            String text, String text2, List<Long> categories, boolean paid,
            LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable);

    Page<Event> findAllByAnnotationContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndCategoryIdInAndPaidAndPublishedOnNotNullAndEventDateBetweenOrderByViewsAsc(
            String text, String text2, List<Long> categories, boolean paid,
            LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable);

    @Transactional(readOnly = true)
    @Query("select e from Event e where e.paid = ?3 ")
    Page<Event> findByParams(String text, List<Long> categories, boolean paid,
                             LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable);
}
