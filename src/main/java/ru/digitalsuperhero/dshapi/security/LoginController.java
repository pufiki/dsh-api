package ru.digitalsuperhero.dshapi.security;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.digitalsuperhero.dshapi.dao.ContractorRepository;
import ru.digitalsuperhero.dshapi.dao.CustomerRepository;
import ru.digitalsuperhero.dshapi.dao.domain.Contractor;
import ru.digitalsuperhero.dshapi.dao.domain.Customer;

@RestController
@RequestMapping("/login")
public class LoginController {

    private CustomerRepository customerRepo;
    private ContractorRepository contractorRepo;
    private PasswordEncoder passwordEncoder;

    public LoginController(
            CustomerRepository customerRepo, ContractorRepository contractorRepo, PasswordEncoder passwordEncoder) {
        this.customerRepo = customerRepo;
        this.contractorRepo = contractorRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping(path = "/customer", consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Customer login(@RequestBody Customer customer) {
        Customer customerFound = customerRepo.findByEmail(customer.getUsername());
        if (customerFound != null && passwordEncoder.matches(customer.getPassword(), customerFound.getPassword())) {
            return customerFound;
        }
        return customer;
    }

    @GetMapping(path = "/contractor", consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Contractor login(@RequestBody Contractor contractor) {
        Contractor contractorFound = contractorRepo.findByEmail(contractor.getUsername());
        if (contractorFound != null && passwordEncoder.matches(contractor.getPassword(), contractorFound.getPassword())) {
            return contractorFound;
        }
        return contractor;
    }
}
