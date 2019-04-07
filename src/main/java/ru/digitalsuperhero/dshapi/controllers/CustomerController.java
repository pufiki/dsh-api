package ru.digitalsuperhero.dshapi.controllers;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.digitalsuperhero.dshapi.dao.CustomerRepository;
import ru.digitalsuperhero.dshapi.dao.domain.Customer;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;
import java.util.Random;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "/customers")
public class CustomerController {
    private static final String ACCOUNT_SID = "AC10dd163c86bc363f0623bfafa9ab2c8b";
    private static final String AUTH_TOKEN = "791e79bf0da449d828901cf993ebc66f";
    private static final String fromNumber = "+17868286905";

    private static final Random RANDOM = new SecureRandom();
    private static final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    private CustomerRepository customerRepository;

    public CustomerController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public static String generatePassword(int length) {
        StringBuilder returnValue = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            returnValue.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }
        return new String(returnValue);
    }

    @GetMapping(produces = "application/json")
    public Resources<CustomerResource> getCustomers() {
        PageRequest pageRequest =
                PageRequest.of(0, 6, Sort.by("id").descending());
        List<Customer> allCustomers = customerRepository.findAll(pageRequest).getContent();
        List<CustomerResource> customerResources =
                new CustomerResourceAssembler().toResources(allCustomers);
        Resources<CustomerResource> recentResources =
                new Resources<CustomerResource>(customerResources);
        return recentResources;
    }

    @PutMapping(path = "/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Customer> putCustomer(@PathVariable("id") Long id, @RequestBody Customer customer) {
        Customer foundCustomer = customerRepository.findById(id).get();
        customerRepository.delete(foundCustomer);
        customer.setId(id);
        return new ResponseEntity<>(customerRepository.save(customer), HttpStatus.ACCEPTED);
    }

    @PatchMapping(path = "/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Customer> patchCustomer(@PathVariable("id") Long id, @RequestBody Customer customerPatch) {
        Customer foundCustomer = customerRepository.findById(id).get();
        if (customerPatch.getPassword() != null) {
            foundCustomer.setPassword(customerPatch.getPassword());
        }
        if (customerPatch.getEmail() != null) {
            foundCustomer.setEmail(customerPatch.getEmail());
        }
        if (customerPatch.getPhoneNumber() != null) {
            foundCustomer.setPhoneNumber(customerPatch.getPhoneNumber());
        }
        if (customerPatch.getName() != null) {
            foundCustomer.setName(customerPatch.getName());
        }
        if (customerPatch.getWorkRequestsCreated() != null) {
            foundCustomer.setWorkRequestsCreated(customerPatch.getWorkRequestsCreated());
        }
        return new ResponseEntity<>(customerRepository.save(foundCustomer), HttpStatus.ACCEPTED);
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCustomer(@PathVariable("id") Long id) {
        try {
            customerRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
        }
    }

    @PostMapping(path = "/register", consumes = "application/json")
    public ResponseEntity<Customer> processRegistration(@RequestBody Customer customer) {
        String password = generatePassword(3);
        String encodedPassword = Base64.getEncoder().encodeToString(password.getBytes());
        customer.setPassword(encodedPassword);
        Customer foundCustomer = customerRepository.findByEmail(customer.getEmail());
        if (foundCustomer == null) {
            sendSmsToCustomer(customer, password);
            return new ResponseEntity<>(customerRepository.save(customer), HttpStatus.OK);
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

    @PostMapping(path = "/signin", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Customer> login(@RequestBody Customer customer) {
        Customer foundCustomer = customerRepository.findByEmail(customer.getEmail());
        byte[] foundContractorDecodedBytes = Base64.getDecoder().decode(foundCustomer.getPassword());
        String decodedFounderPassword = new String(foundContractorDecodedBytes);
        if (foundCustomer != null && customer.getPassword().equals(decodedFounderPassword)) {
            foundCustomer.setSignedIn(true);
            return new ResponseEntity<>(foundCustomer, HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>(customer, HttpStatus.NOT_FOUND);
        }
    }
}
