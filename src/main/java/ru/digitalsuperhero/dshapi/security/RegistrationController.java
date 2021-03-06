package ru.digitalsuperhero.dshapi.security;


import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.digitalsuperhero.dshapi.dao.ContractorRepository;
import ru.digitalsuperhero.dshapi.dao.CustomerRepository;
import ru.digitalsuperhero.dshapi.dao.domain.Contractor;
import ru.digitalsuperhero.dshapi.dao.domain.Customer;

import java.security.SecureRandom;
import java.util.Random;

@RestController
@RequestMapping("/register")
//@CrossOrigin(origins = "*")
public class RegistrationController {

    private static final String ACCOUNT_SID = "AC10dd163c86bc363f0623bfafa9ab2c8b";
    private static final String AUTH_TOKEN = "791e79bf0da449d828901cf993ebc66f";
    private static final String fromNumber = "+17868286905";

    private static final Random RANDOM = new SecureRandom();
    private static final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    private CustomerRepository customerRepo;
    private ContractorRepository contractorRepo;
    private PasswordEncoder passwordEncoder;

    public RegistrationController(
            CustomerRepository customerRepo, ContractorRepository contractorRepo, PasswordEncoder passwordEncoder) {
        this.customerRepo = customerRepo;
        this.contractorRepo = contractorRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public static String generatePassword(int length) {
        StringBuilder returnValue = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            returnValue.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }
        return new String(returnValue);
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

    @PostMapping(path = "/customer", consumes = "application/json") //post mapping
    @CrossOrigin(origins = {"https://pufiki.herokuapp.com", "http://pufiki.herokuapp.com", "http://localhost:3000"})
    public ResponseEntity<Customer> processRegistration(@RequestBody Customer customer) {
        String password = generatePassword(3);
        customer.setPassword(passwordEncoder.encode(password));
        Customer foundCustomer = customerRepo.findByEmail(customer.getEmail());
        if (foundCustomer == null) {
            sendSmsToCustomer(customer, password);
            return new ResponseEntity<>(customerRepo.save(customer), HttpStatus.OK);
        }
        return new ResponseEntity<>(foundCustomer, HttpStatus.CONFLICT);
    }

    private void sendSmsToCustomer(Customer customer, String password) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Message message = Message.creator(new PhoneNumber(customer.getPhoneNumber()),
                new PhoneNumber(fromNumber),
                "Почта: " + customer.getEmail() +
                        ". Автоматически сгенерированный пароль (его знаете только вы): " + customer.getPassword() + " .").create();
        System.out.println("=======>>> sent " + message.getDateSent());
    }
}