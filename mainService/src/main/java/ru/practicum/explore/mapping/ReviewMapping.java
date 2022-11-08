package ru.practicum.explore.mapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.explore.dto.NewReview;
import ru.practicum.explore.dto.ReviewDto;
import ru.practicum.explore.model.Review;
import ru.practicum.explore.service.ReviewService;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewMapping {
    private final ReviewService reviewService;

    public static Review fromNewToReview(NewReview newReview) {
        Review review = new Review();
        review.setText(newReview.getText());
        review.setEventRating(newReview.getEventRating());
        return review;
    }

    public static ReviewDto fromReviewToDto(Review review) {
        ReviewDto reviewDto = new ReviewDto();
        reviewDto.setAuthorName(review.getAuthor().getName());
        reviewDto.setEventTitle(review.getEvent().getTitle());
        reviewDto.setText(review.getText());
        reviewDto.setId(review.getId());
        reviewDto.setEventRating(reviewDto.getEventRating());
        return reviewDto;
    }

    public ReviewDto postReview(long userId, long eventId, NewReview newReview) {
        Review review = fromNewToReview(newReview);
        Review backReview = reviewService.postReview(userId, eventId, review);
        ReviewDto reviewDto = fromReviewToDto(backReview);
        return reviewDto;
    }

    public void deleteReview(long userId, long eventId, long reviewId) {
        reviewService.deleteReview(userId, eventId, reviewId);
    }

    public void rateReview(long userId, long eventId, long reviewId, Integer value) {
        reviewService.rateReview(userId, eventId, reviewId, value);
    }

    public List<ReviewDto> getReviews(long eventId, Integer from, Integer size, String sort) {
        List<Review> reviews = reviewService.getReviews(eventId, from, size, sort);
        List<ReviewDto> result = reviews.stream().map(ReviewMapping::fromReviewToDto).collect(Collectors.toList());
        return result;
    }
}
