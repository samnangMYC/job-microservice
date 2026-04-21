package com.samnang.reviewms.review;

import com.samnang.reviewms.messaging.ReviewMessageProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService{

    private final ReviewRepository reviewRepository;
    private final ReviewMessageProducer reviewMessageProducer;


    @Override
    public List<Review> getAllReviews(Long companyId) {
        return reviewRepository.findAll();
    }

    @Override
    public List<Review> getReviewsByCompanyId(Long companyId) {
        return reviewRepository.findByCompanyId(companyId);
    }

    @Override
    public Review getReviewById(Long id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found with id: " + id));
    }

    @Override
    public Review createReview(Long companyId, Review review) {
        reviewMessageProducer.sendMessage(review);
        review.setCompanyId(companyId);
        return reviewRepository.save(review);
    }

    @Override
    public Review updateReview(Long id, Review updatedReview) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found with id: " + id));

        review.setReviewerName(updatedReview.getReviewerName());
        review.setComment(updatedReview.getComment());
        review.setRating(updatedReview.getRating());
        review.setCompanyId(updatedReview.getCompanyId());
        return reviewRepository.save(review);
    }

    @Override
    public void deleteReview(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found with id: " + id));

        reviewRepository.delete(review);
    }

}
