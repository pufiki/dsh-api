package ru.digitalsuperhero.dshapi.security;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.digitalsuperhero.dshapi.dao.ContractorRepository;
import ru.digitalsuperhero.dshapi.dao.CustomerRepository;
import ru.digitalsuperhero.dshapi.dao.domain.Contractor;
import ru.digitalsuperhero.dshapi.dao.domain.Customer;

@RestController
@RequestMapping("/register")
@CrossOrigin(origins = "*")
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
    public ResponseEntity<Contractor> processRegistration(@RequestBody Contractor contractor) {
        contractor.setPassword(passwordEncoder.encode(contractor.getPassword()));
        Contractor foundContractor = contractorRepo.findByEmail(contractor.getEmail());
        if (foundContractor == null) {
            return new ResponseEntity<>(contractorRepo.save(contractor), HttpStatus.OK);
        }
        return new ResponseEntity<>(foundContractor, HttpStatus.CONFLICT);
    }

    @PostMapping(path = "/customer", consumes = "application/json")
    public ResponseEntity<Customer> processRegistration(@RequestBody Customer customer) {
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        Customer foundCustomer = customerRepo.findByEmail(customer.getEmail());
        if (foundCustomer == null) {
            return new ResponseEntity<>(customerRepo.save(customer), HttpStatus.OK);
        }
        return new ResponseEntity<>(foundCustomer, HttpStatus.CONFLICT);
    }
}