package ru.digitalsuperhero.dshapi.controllers;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.digitalsuperhero.dshapi.dao.ReviewRepository;
import ru.digitalsuperhero.dshapi.dao.domain.Review;

@RepositoryRestController
@RequestMapping(path = "/review")
@CrossOrigin(origins = "*")
public class ReviewController {
    private ReviewRepository reviewRepository;

    public ReviewController(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<Review> postWorkRequest(@RequestBody Review review) {
        Review foundReview = reviewRepository.findById((review.getId())).get();
        if (foundReview == null) {
            return new ResponseEntity<>(reviewRepository.save(review), HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>(foundReview, HttpStatus.CONFLICT);
    }

    @GetMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<Review> getMessages(@PathVariable("id") Long id) {
        Review foundReview = reviewRepository.findById(id).get();
        if (foundReview != null) {
            return new ResponseEntity<Review>(foundReview, HttpStatus.OK);
        }
        return new ResponseEntity<Review>(foundReview, HttpStatus.NOT_FOUND);
    }

    @PatchMapping(path = "/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Review> patchContractor(@PathVariable("id") Long id, @RequestBody Review review) {
        Review foundReview = reviewRepository.findById(id).get();
        if (review.getId() != null) {
            foundReview.setId(review.getId());
        }
        if (review.getDescription() != null) {
            foundReview.setDescription(review.getDescription());
        }
        if (review.getRatingStars() != null) {
            foundReview.setRatingStars(review.getRatingStars());
        }
        return new ResponseEntity<>(reviewRepository.save(foundReview), HttpStatus.ACCEPTED);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCustomer(@PathVariable("id") Long id) {
        try {
            reviewRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
        }
    }
}
