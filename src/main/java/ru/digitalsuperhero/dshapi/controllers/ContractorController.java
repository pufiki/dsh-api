package ru.digitalsuperhero.dshapi.controllers;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.digitalsuperhero.dshapi.dao.ContractorRepository;
import ru.digitalsuperhero.dshapi.dao.domain.Contractor;
import ru.digitalsuperhero.dshapi.dao.domain.Customer;

import java.util.Base64;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping(path = "/contractors")
@CrossOrigin(origins = "*")
public class ContractorController {
    private ContractorRepository contractorRepository;

    public ContractorController(ContractorRepository contractorRepository) {
        this.contractorRepository = contractorRepository;
    }

    @GetMapping(produces = "application/json")
    public Resources<Resource<Contractor>> getContractors() {
        PageRequest page = PageRequest.of(
                0, 12, Sort.by("id").descending());
        List<Contractor> allContractors = contractorRepository.findAll(page).getContent();
        Resources<Resource<Contractor>> recentResources = Resources.wrap(allContractors);
        recentResources.add(
                linkTo(methodOn(CustomerController.class).getCustomers())
                        .withRel("recents"));
        return recentResources;
    }

    @PutMapping(path = "/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Contractor> putContractor(@PathVariable("id") Long id, @RequestBody Contractor contractor) {
        Contractor foundContractor = contractorRepository.findById(id).get();
        contractorRepository.delete(foundContractor);
        contractor.setId(id);
        return new ResponseEntity<>(contractorRepository.save(contractor), HttpStatus.ACCEPTED);
    }

    @PatchMapping(path = "/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Contractor> patchContractor(@PathVariable("id") Long id, @RequestBody Contractor contractor) {
        Contractor foundContractor = contractorRepository.findById(id).get();
        if (contractor.getPassword() != null) {
            foundContractor.setPassword(contractor.getPassword());
        }
        if (contractor.getEmail() != null) {
            foundContractor.setEmail(contractor.getEmail());
        }
        if (contractor.getCompanyName() != null) {
            foundContractor.setCompanyName(contractor.getCompanyName());
        }
        if (contractor.getWorkRequests() != null) {
            foundContractor.setWorkRequests(contractor.getWorkRequests());
        }
        return new ResponseEntity<>(contractorRepository.save(foundContractor), HttpStatus.ACCEPTED);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteContractor(@PathVariable("id") Long id) {
        try {
            contractorRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
        }
    }

    @PostMapping(path = "/register", consumes = "application/json")
    public ResponseEntity<Contractor> processRegistration(@RequestBody Contractor contractor) {
        String encodedPassword = Base64.getEncoder().encodeToString(contractor.getPassword().getBytes());
        contractor.setPassword(encodedPassword);
        Contractor foundContractor = contractorRepository.findByEmail(contractor.getEmail());
        if (foundContractor == null) {
            return new ResponseEntity<>(contractorRepository.save(contractor), HttpStatus.OK);
        }
        return new ResponseEntity<>(foundContractor, HttpStatus.CONFLICT);
    }

    @PostMapping(path = "/signin", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Contractor> login(@RequestBody Contractor contractor) {
        Contractor foundContractor = contractorRepository.findByEmail(contractor.getEmail());
        byte[] foundContractorDecodedBytes = Base64.getDecoder().decode(foundContractor.getPassword());
        String decodedFounderPassword = new String(foundContractorDecodedBytes);
        if (foundContractor != null && contractor.getPassword().equals(decodedFounderPassword)) {
            foundContractor.setSignedIn(true);
            return new ResponseEntity<>(foundContractor, HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>(contractor, HttpStatus.NOT_FOUND);
        }
    }
}
