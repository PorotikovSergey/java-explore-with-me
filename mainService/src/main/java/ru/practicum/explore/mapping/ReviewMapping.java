package ru.practicum.explore.mapping;

import ru.practicum.explore.dto.NewReview;
import ru.practicum.explore.dto.ReviewDto;
import ru.practicum.explore.model.Review;

public class ReviewMapping {

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
}
