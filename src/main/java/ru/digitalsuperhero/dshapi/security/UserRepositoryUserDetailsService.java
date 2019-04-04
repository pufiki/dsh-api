package ru.digitalsuperhero.dshapi.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.digitalsuperhero.dshapi.dao.ContractorRepository;
import ru.digitalsuperhero.dshapi.dao.CustomerRepository;
import ru.digitalsuperhero.dshapi.dao.domain.Contractor;
import ru.digitalsuperhero.dshapi.dao.domain.Customer;

@Service
public class UserRepositoryUserDetailsService
        implements UserDetailsService {
    private CustomerRepository customerRepo;
    private ContractorRepository contractorRepo;

    @Autowired
    public UserRepositoryUserDetailsService(CustomerRepository customerRepo, ContractorRepository contractorRepo) {
        this.customerRepo = customerRepo;
        this.contractorRepo = contractorRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
        Customer customer = customerRepo.findByEmail(email);
        if (customer != null) {
            return customer;
        }
        Contractor contractor = contractorRepo.findByEmail(email);
        if (contractor != null) {
            return contractor;
        }
        throw new UsernameNotFoundException(
                "User '" + email + "' not found");
    }
}
