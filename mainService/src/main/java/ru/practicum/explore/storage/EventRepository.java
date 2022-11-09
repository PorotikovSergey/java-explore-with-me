package ru.practicum.explore.storage;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Query("select e from Event e where (:users is null OR e.owner.id IN :users) " +
            "AND (:states is null OR e.state IN :states) " +
            "AND (:cat is null OR e.category.id IN :cat) " +
            "AND (e.eventDate BETWEEN :start AND :end)")
    Page<Event> findByParamsAdmin(@Param("users") List<Long> users, @Param("states") List<String> states,
                                  @Param("cat") List<Long> categories, @Param("start") LocalDateTime rangeStart,
                                  @Param("end") LocalDateTime rangeEnd, Pageable pageable);

    @Query("select e from Event e where (:pay is null OR e.paid = :pay) " +
            "AND (:text is null OR ((lower(e.annotation) LIKE :text OR lower(e.description) LIKE :text))) " +
            "AND (:cat is null OR e.category.id IN :cat) " +
            "AND (e.eventDate BETWEEN :start AND :end) " +
            "AND (:onlyAv is null OR e.publishedOn is not null)")
    Page<Event> findByParams(@Param("text") String text, @Param("cat") List<Long> categories,
                             @Param("pay") Boolean paid, @Param("start") LocalDateTime rangeStart,
                             @Param("end") LocalDateTime rangeEnd, @Param("onlyAv") Boolean onlyAv, Pageable pageable);
}
