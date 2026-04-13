package com.samnang.reviewms.review;

import java.util.List;

public interface ReviewService {
    List<Review> getAllReviews();

    Review getReviewById(Long id);

    List<Review> getReviewsByCompanyId(Long companyId);

    Review createReview(Long companyId, Review review);

    Review updateReview(Long id, Review review);

    void deleteReview(Long id);
}
