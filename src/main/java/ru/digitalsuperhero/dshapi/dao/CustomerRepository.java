package ru.digitalsuperhero.dshapi.dao;

import org.springframework.data.repository.PagingAndSortingRepository;
import ru.digitalsuperhero.dshapi.dao.domain.Customer;

import java.util.Optional;

public interface CustomerRepository extends PagingAndSortingRepository<Customer, Long> {

    Customer findByEmail(String email);
}
