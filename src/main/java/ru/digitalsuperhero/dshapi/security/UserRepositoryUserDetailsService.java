package ru.digitalsuperhero.dshapi.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.digitalsuperhero.dshapi.dao.AdminRepository;
import ru.digitalsuperhero.dshapi.dao.ContractorRepository;
import ru.digitalsuperhero.dshapi.dao.CustomerRepository;
import ru.digitalsuperhero.dshapi.dao.domain.Admin;
import ru.digitalsuperhero.dshapi.dao.domain.Contractor;
import ru.digitalsuperhero.dshapi.dao.domain.Customer;

@Service
public class UserRepositoryUserDetailsService
        implements UserDetailsService {
    private CustomerRepository customerRepo;
    private ContractorRepository contractorRepo;
    private AdminRepository adminRepo;

    @Autowired
    public UserRepositoryUserDetailsService(CustomerRepository customerRepo, ContractorRepository contractorRepo, AdminRepository adminRepo) {
        this.customerRepo = customerRepo;
        this.contractorRepo = contractorRepo;
        this.adminRepo = adminRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        Customer customer = customerRepo.findByEmail(username);
        if (customer != null) {
            return customer;
        }
        Contractor contractor = contractorRepo.findByEmail(username);
        if (contractor != null) {
            return contractor;
        }
        Admin admin = adminRepo.findByUsername(username);
        if (admin != null) {
            return admin;
        }
        throw new UsernameNotFoundException(
                "User '" + username + "' not found");
    }
}
