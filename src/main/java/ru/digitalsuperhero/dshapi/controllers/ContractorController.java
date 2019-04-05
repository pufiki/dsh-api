package ru.digitalsuperhero.dshapi.controllers;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
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

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteContractor(@PathVariable("id") Long id) {
        try {
            contractorRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
        }
    }
}
