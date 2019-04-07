package ru.digitalsuperhero.dshapi.dao;

import org.springframework.data.repository.CrudRepository;
import ru.digitalsuperhero.dshapi.dao.domain.Review;

import java.util.Optional;

public interface ReviewRepository extends CrudRepository<Review, Long> {
    Optional<Review> findById(Long id);
}
