package ru.practicum.explore.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.explore.enums.EventState;
import ru.practicum.explore.exceptions.NotFoundException;
import ru.practicum.explore.exceptions.ValidationException;
import ru.practicum.explore.model.Event;
import ru.practicum.explore.model.Review;
import ru.practicum.explore.model.User;
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

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("События с таким id " + eventId + " нет в БД"));
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Юзера с таким id " + userId + " нет в БД"));

        review.setAuthor(author);
        review.setEvent(event);
        review.setCreatedOn(LocalDateTime.now());

        if (event.getEventDate().isAfter(review.getCreatedOn())) {
            log.warn("!!!отзыв оставляется на событие из будущего!!!");
//            throw new ValidationException("Нельзя оставлять отзыв на ещё несостоявшееся событие");
        }

        if (reviewRepository.findByEventIdAndAndAuthorId(eventId, userId) != null) {
            throw new ValidationException("Похоже вы уже оставляли ваш отзыв о данном событии");
        }

        reviewRepository.save(review);
        log.info("Вот такой отзыв по итогу сохранён в бд: {}", review);
        return review;
    }

    public void deleteReview(long userId, long eventId, long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new NotFoundException("Отзыва с таким id " + reviewId + " нет в БД"));

        if (review.getEvent().getId() != eventId) {
            throw new ValidationException("Событие в запросе и в отзыве не совпадают");
        }

        if (review.getAuthor().getId() != userId) {
            throw new ValidationException("Нельзя удалять чужой комментарий");
        }

        reviewRepository.deleteById(reviewId);
    }

    public void deleteReviewByAdmin(long reviewId) {
        //админ может удалять абсолютно любой коммент
        reviewRepository.deleteById(reviewId);
    }

    public void patchReview(long reviewId, boolean decision) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new NotFoundException("Отзыва с таким id " + reviewId + " нет в БД"));

        if (decision) {
            review.setState(EventState.PUBLISHED.toString());
            log.info("Отзыв опубликован: {}", review);
        } else {
            review.setState(EventState.CANCELED.toString());
            log.info("Отзыв отменён: {}", review);
        }

        reviewRepository.save(review);
    }

    public void rateReview(long userId, long eventId, long reviewId, Integer value) {
        checkIds(userId, eventId);
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new NotFoundException("Отзыва с таким id " + reviewId + " нет в БД"));
        if (review.getAuthor().getId() == userId) {
            throw new ValidationException("Нельзя ставить оценки своим комментариям");
        }
        if (!review.getState().equals(EventState.PUBLISHED.toString())) {
            throw new ValidationException("Вы пытаетесь поставить оценку комментарию не в статусе PUBLISHED");
        }
        long counter = review.getCounter();
        float reviewValue = review.getCommentRating();
        //эта строчка высчитывают среднее арифметическое у рейтинга комментария.
        //По этому параметру мы сможем сортировать комменты по полезности, если захотим
        float result;
        if (counter != 0) {
            result = ((reviewValue * counter) + value) / (++counter);
            review.setCounter(counter);
        } else {
            result = value;
            review.setCounter(1);
        }
        review.setCommentRating(result);
        log.info("в бд сохраняется обновлённый отзыв: " + review);
        reviewRepository.save(review);
    }

    public List<Review> getReviews(long eventId, Integer from, Integer size, String sort) {
        Pageable pageable = PageRequest.of(from, size);
        //вот тут мы получаем список комментов к событию и сортируем
        //Либо по рейтингу коммента, иначе по дате создания
        //Причём получаем только опубликованные
        if (sort.equals("REVIEW_RATING")) {
            Page<Review> list = reviewRepository
                    .findAllByEventIdAndStateOrderByCommentRatingDesc(eventId, "PUBLISHED", pageable);
            return list.getContent();
        } else {
            Page<Review> list = reviewRepository
                    .findAllByEventIdAndStateOrderByCreatedOnDesc(eventId, "PUBLISHED", pageable);
            return list.getContent();
        }
    }

    private void checkIds(long userId, long eventId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Юзера с таким id: " + userId + " нет в БД");
        }
        if (!eventRepository.existsById(eventId)) {
            throw new NotFoundException("События с таким id: " + userId + " нет в БД");
        }
    }
}
