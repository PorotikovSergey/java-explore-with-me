package ru.practicum.explore.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.explore.exceptions.NotFoundException;
import ru.practicum.explore.exceptions.ValidationException;
import ru.practicum.explore.model.Event;
import ru.practicum.explore.model.Review;
import ru.practicum.explore.storage.EventRepository;
import ru.practicum.explore.storage.ReviewRepository;
import ru.practicum.explore.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final ReviewRepository reviewRepository;

    public Review postReview(long userId, long eventId, Review review) {
        checkIds(userId, eventId);

        review.setCreatedOn(LocalDateTime.now());
        review.setAuthor(userRepository.findById(userId).get());
        review.setEvent(eventRepository.findById(eventId).get());
        review.setCreatedOn(LocalDateTime.now());
        Event event = eventRepository.findById(eventId).get();

        if(event.getEventDate().isAfter(review.getCreatedOn())) {
            throw new ValidationException("Нельзя оставлять отзыв на ещё несостоявшееся событие");
        }

        reviewRepository.save(review);
        return review;
    }

    public void deleteReview(long userId, long eventId, long reviewId) {
        checkIds(userId, eventId);
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new NotFoundException("Отзыва с таким id " + reviewId + " нет в БД"));

        if(review.getAuthor().getId() != userId) {
            throw new ValidationException("Нельзя удалять чужой комментарий");
        }

        reviewRepository.deleteById(reviewId);
    }

    public void rateReview(long userId, long eventId, long reviewId, Integer value) {
        checkIds(userId, eventId);
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new NotFoundException("Отзыва с таким id " + reviewId + " нет в БД"));

        if(review.getAuthor().getId() == userId) {
            throw new ValidationException("Нельзя ставить оценки своим комментариям");
        }

        long counter = review.getCounter();
        float reviewValue = review.getCommentRating();

        float result = ((reviewValue * counter)+value)/(++counter);
        review.setCommentRating(result);
    }

    private void checkIds(long userId, long eventId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Юзера с таким id: " + userId + " нет в БД");
        }
        if ((eventRepository.existsById(eventId))) {
            throw new NotFoundException("События с таким id: " + userId + " нет в БД");
        }
    }


    public List<Review> getReviews(long eventId, Integer from, Integer size, String sort) {

        if(sort.equals("REVIEW_RATING")) {
            Pageable pageable = PageRequest.of(from, size, Sort.by("review_rating").ascending());
            Page<Review> list = reviewRepository.findAllByEventId(eventId, pageable);
            return list.getContent();
        } else {
            Pageable pageable = PageRequest.of(from, size, Sort.by("created_on").descending());
            Page<Review> list = reviewRepository.findAllByEventId(eventId, pageable);
            return list.getContent();
        }
    }
}
