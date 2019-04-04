package ru.digitalsuperhero.dshapi.dao;

import org.springframework.data.repository.CrudRepository;
import ru.digitalsuperhero.dshapi.dao.domain.Customer;

public interface CustomerRepository extends CrudRepository<Customer, Long> {

    Customer findByEmail(String email);
}
