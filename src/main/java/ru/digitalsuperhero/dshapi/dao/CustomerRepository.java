package ru.digitalsuperhero.dshapi.dao;

import org.springframework.data.repository.PagingAndSortingRepository;
import ru.digitalsuperhero.dshapi.dao.domain.Customer;

public interface CustomerRepository extends PagingAndSortingRepository<Customer, Long> {

    Customer findByEmail(String email);
}
