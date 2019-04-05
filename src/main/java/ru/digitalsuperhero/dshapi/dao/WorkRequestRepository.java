package ru.digitalsuperhero.dshapi.dao;

import org.springframework.data.repository.PagingAndSortingRepository;
import ru.digitalsuperhero.dshapi.dao.domain.Contractor;
import ru.digitalsuperhero.dshapi.dao.domain.WorkRequest;

public interface WorkRequestRepository extends PagingAndSortingRepository<WorkRequest, Long> {
    Contractor findByName(String name);
}
