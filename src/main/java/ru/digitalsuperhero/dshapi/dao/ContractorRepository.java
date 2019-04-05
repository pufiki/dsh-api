package ru.digitalsuperhero.dshapi.dao;

import org.springframework.data.repository.PagingAndSortingRepository;
import ru.digitalsuperhero.dshapi.dao.domain.Contractor;

public interface ContractorRepository extends PagingAndSortingRepository<Contractor, Long> {
    Contractor findByEmail(String email);
}
