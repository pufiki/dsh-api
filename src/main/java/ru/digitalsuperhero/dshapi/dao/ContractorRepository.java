package ru.digitalsuperhero.dshapi.dao;

import org.springframework.data.repository.CrudRepository;
import ru.digitalsuperhero.dshapi.dao.domain.Contractor;

public interface ContractorRepository extends CrudRepository<Contractor, Long> {
    Contractor findByEmail(String email);
}
