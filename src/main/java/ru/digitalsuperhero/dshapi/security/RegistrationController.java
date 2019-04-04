package ru.digitalsuperhero.dshapi.security;


import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.digitalsuperhero.dshapi.dao.ContractorRepository;
import ru.digitalsuperhero.dshapi.dao.CustomerRepository;
import ru.digitalsuperhero.dshapi.dao.domain.Contractor;
import ru.digitalsuperhero.dshapi.dao.domain.Customer;

@RestController
@RequestMapping("/register")
public class RegistrationController {

    private CustomerRepository customerRepo;
    private ContractorRepository contractorRepo;
    private PasswordEncoder passwordEncoder;

    public RegistrationController(
            CustomerRepository customerRepo, ContractorRepository contractorRepo, PasswordEncoder passwordEncoder) {
        this.customerRepo = customerRepo;
        this.contractorRepo = contractorRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping(path = "/contractor", consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public Contractor processRegistration(@RequestBody Contractor contractor) {
        contractor.setPassword(passwordEncoder.encode(contractor.getPassword()));
        return contractorRepo.save(contractor);
    }

    @PostMapping(path = "/customer", consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public Customer processRegistration(@RequestBody Customer customer) {
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        return customerRepo.save(customer);
    }
}