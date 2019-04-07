package ru.digitalsuperhero.dshapi.dao;

import org.springframework.data.repository.CrudRepository;
import ru.digitalsuperhero.dshapi.dao.domain.Message;

import java.util.Optional;

public interface MessageRepository extends CrudRepository<Message, Long> {
    Optional<Message> findById(Long id);
}
