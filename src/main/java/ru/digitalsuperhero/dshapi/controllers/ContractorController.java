package ru.digitalsuperhero.dshapi.controllers;

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
        if (contractor.getCommercialOffers() != null) {
            foundContractor.setCommercialOffers(contractor.getCommercialOffers());
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
}
