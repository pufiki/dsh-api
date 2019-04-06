package ru.digitalsuperhero.dshapi.dao;

import org.springframework.data.repository.CrudRepository;
import ru.digitalsuperhero.dshapi.dao.domain.Admin;

public interface AdminRepository extends CrudRepository<Admin, Long> {
    public Admin findByUsername(String username);
}
