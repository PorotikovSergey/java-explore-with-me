package ru.practicum.explore.storage;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.explore.model.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    Page<Review> findAllByEventId(long id, Pageable pageable);

    Page<Review> findAllByEventIdAndStateOrderByCreatedOnDesc(long id, String state, Pageable pageable);

    Page<Review> findAllByEventIdAndStateOrderByCommentRatingDesc(long id, String state, Pageable pageable);
}
