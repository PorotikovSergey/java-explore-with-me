package ru.practicum.explore.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.explore.enums.EventState;
import ru.practicum.explore.exceptions.NotFoundException;
import ru.practicum.explore.exceptions.ValidationException;
import ru.practicum.explore.model.Event;
import ru.practicum.explore.model.Review;
import ru.practicum.explore.storage.EventRepository;
import ru.practicum.explore.storage.ReviewRepository;
import ru.practicum.explore.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
        //вот тут оставил этот вариант, так как чуть выше уже проверил существование по айдишникам
        //но могу изменить на выброс ошибки .orElseThrow() ->
        review.setAuthor(userRepository.findById(userId).get());
        review.setEvent(eventRepository.findById(eventId).get());
        review.setCreatedOn(LocalDateTime.now());
        Event event = eventRepository.findById(eventId).get();

        if (event.getEventDate().isAfter(review.getCreatedOn())) {
            throw new ValidationException("Нельзя оставлять отзыв на ещё несостоявшееся событие");
        }

        reviewRepository.save(review);
        log.info("Вот такой отзыв по итогу сохранён в бд: {}", review);
        return review;
    }

    public void deleteReview(long userId, long eventId, long reviewId) {
        checkIds(userId, eventId);
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new NotFoundException("Отзыва с таким id " + reviewId + " нет в БД"));

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
            throw new ValidationException("Вы пытаетесь поставить оценку комментарию в состоянии ожидания публикации");
        }

        //следующие 2 строчки высчитывают средне арифметическое у рейтинга комментария.
        //По этому параметру мы сможем сортировать комменты по полезности, если захотим
        long counter = review.getCounter();
        float reviewValue = review.getCommentRating();

        float result = ((reviewValue * counter) + value) / (++counter);
        review.setCommentRating(result);
        reviewRepository.save(review);
    }

    public List<Review> getReviews(long eventId, Integer from, Integer size, String sort) {
        //вот тут мы получаем список комментов к событию и сортируем
        //Либо по рейтингу коммента, иначе по дате создания
        //Причём получаем только опубликованные
        if (sort.equals("REVIEW_RATING")) {
            Pageable pageable = PageRequest.of(from, size, Sort.by("review_rating").ascending());
            Page<Review> list = reviewRepository.findAllByEventId(eventId, pageable);
            return list.getContent().stream()
                    .filter(r -> r.getState().equals(EventState.PUBLISHED.toString())).collect(Collectors.toList());
        } else {
            Pageable pageable = PageRequest.of(from, size, Sort.by("created_on").descending());
            Page<Review> list = reviewRepository.findAllByEventId(eventId, pageable);
            return list.getContent().stream()
                    .filter(r -> r.getState().equals(EventState.PUBLISHED.toString())).collect(Collectors.toList());
        }
    }

    private void checkIds(long userId, long eventId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Юзера с таким id: " + userId + " нет в БД");
        }
        if ((eventRepository.existsById(eventId))) {
            throw new NotFoundException("События с таким id: " + userId + " нет в БД");
        }
    }
}
