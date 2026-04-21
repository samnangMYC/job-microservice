package com.samnang.reviewms.review;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/{id}")
    public ResponseEntity<Review> getReviewById(@PathVariable Long id) {
        return ResponseEntity.ok(reviewService.getReviewById(id));
    }

    @GetMapping
    public ResponseEntity<List<Review>> getReviewsByCompanyId(@RequestParam Long companyId) {
        return ResponseEntity.ok(reviewService.getReviewsByCompanyId(companyId));
    }

    @PostMapping
    public ResponseEntity<Review> createReview(
            @RequestParam Long companyId,
            @RequestBody Review review
    ) {
        return ResponseEntity.ok(reviewService.createReview(companyId, review));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Review> updateReview(
            @PathVariable Long id,
            @RequestBody Review review
    ) {
        return ResponseEntity.ok(reviewService.updateReview(id, review));
    }

    @GetMapping("/averageRating")
    public Double getAverageReview(@RequestParam Long companyId){
        List<Review> reviewsList = reviewService.getAllReviews(companyId);
        return reviewsList.stream().mapToDouble(Review::getRating).average()
                .orElse(0.0);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return ResponseEntity.ok("Review deleted successfully");
    }

}
