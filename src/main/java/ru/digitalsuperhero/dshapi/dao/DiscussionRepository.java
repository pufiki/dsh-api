package ru.digitalsuperhero.dshapi.dao;

import org.springframework.data.repository.CrudRepository;
import ru.digitalsuperhero.dshapi.dao.domain.Discussion;

import java.util.Optional;

public interface DiscussionRepository extends CrudRepository<Discussion, Long> {
    Optional<Discussion> findById(Long id);
}
