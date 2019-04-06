package ru.digitalsuperhero.dshapi.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.digitalsuperhero.dshapi.dao.ContractorRepository;
import ru.digitalsuperhero.dshapi.dao.CustomerRepository;
import ru.digitalsuperhero.dshapi.dao.domain.Contractor;
import ru.digitalsuperhero.dshapi.dao.domain.Customer;
import ru.digitalsuperhero.dshapi.security.jwt.JwtTokenProvider;

import java.util.HashMap;
import java.util.Map;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/login")
@CrossOrigin(origins = "*")
public class LoginController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

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
    public ResponseEntity<Customer> login(@RequestBody Customer customerAuthenticationRequest) {
        String username = customerAuthenticationRequest.getEmail();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(customerAuthenticationRequest.getEmail(), customerAuthenticationRequest.getPassword()));
        Customer foundCustomer = customerRepo.findByEmail(username);
        if (foundCustomer != null) {
            String token = jwtTokenProvider.createToken(username, customerRepo.findByEmail(username).getRoles());
            CustomerAuthenticatedResponse customerAuthenticatedResponse = new CustomerAuthenticatedResponse(foundCustomer, token);
            return new ResponseEntity<>(customerAuthenticatedResponse, HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>(customerAuthenticationRequest, HttpStatus.NOT_FOUND);
        }
    }

//    @GetMapping("/me")
//    public ResponseEntity currentUser(@AuthenticationPrincipal UserDetails userDetails) {
//        Map<Object, Object> model = new HashMap<>();
//        model.put("username", userDetails.getUsername());
//        model.put("roles", userDetails.getAuthorities()
//                .stream()
//                .map(a -> ((GrantedAuthority) a).getAuthority())
//                .collect(toList())
//        );
//        return ok(model);
//    }

    @GetMapping(path = "/contractor", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Contractor> login(@RequestBody Contractor contractor) {
        String username = contractor.getEmail();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(contractor.getEmail(), contractor.getPassword()));
        Contractor foundContractor = contractorRepo.findByEmail(username);
        if (foundContractor != null) {
            String token = jwtTokenProvider.createToken(username, contractorRepo.findByEmail(username).getRoles());
            ContractorAuthenticatedResponse contractorAuthenticatedResponse = new ContractorAuthenticatedResponse(foundContractor, token);
            return new ResponseEntity<>(contractorAuthenticatedResponse, HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>(contractor, HttpStatus.NOT_FOUND);
        }
    }
}
