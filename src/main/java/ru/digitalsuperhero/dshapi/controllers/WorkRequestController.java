package ru.digitalsuperhero.dshapi.controllers;


import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.digitalsuperhero.dshapi.dao.WorkRequestRepository;
import ru.digitalsuperhero.dshapi.dao.domain.WorkRequest;

import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping(path = "/workRequests")
@CrossOrigin(origins = "*")
public class WorkRequestController {

    private WorkRequestRepository workRequestRepository;

    public WorkRequestController(WorkRequestRepository workRequestRepository) {
        this.workRequestRepository = workRequestRepository;
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<WorkRequest> postWorkRequest(@RequestBody WorkRequest workRequest) {
        WorkRequest foundWorkRequest = workRequestRepository.findByName(workRequest.getName());
        if (foundWorkRequest == null) {
            return new ResponseEntity<>(workRequestRepository.save(workRequest), HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>(foundWorkRequest, HttpStatus.CONFLICT);
    }

    @GetMapping(produces = "application/json")
    public Resources<Resource<WorkRequest>> getWorkRequests() {
        PageRequest page = PageRequest.of(
                0, 12, Sort.by("id").descending());
        List<WorkRequest> allWorkRequests = workRequestRepository.findAll(page).getContent();
        Resources<Resource<WorkRequest>> recentResources = Resources.wrap(allWorkRequests);
        recentResources.add(
                linkTo(methodOn(CustomerController.class).getCustomers())
                        .withRel("recents"));
        return recentResources;
    }

    @PutMapping(path = "/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<WorkRequest> putContractor(@PathVariable("id") Long id, @RequestBody WorkRequest workRequest) {
        WorkRequest foundWorkRequest = workRequestRepository.findById(id).get();
        workRequestRepository.delete(foundWorkRequest);
        workRequest.setId(id);
        return new ResponseEntity<>(workRequestRepository.save(workRequest), HttpStatus.ACCEPTED);
    }

    @PatchMapping(path = "/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<WorkRequest> patchContractor(@PathVariable("id") Long id, @RequestBody WorkRequest workRequest) {
        WorkRequest foundWorkRequest = workRequestRepository.findById(id).get();
        if (workRequest.getName() != null) {
            foundWorkRequest.setName(workRequest.getName());
        }
        if (workRequest.getDescription() != null) {
            foundWorkRequest.setDescription(workRequest.getDescription());
        }
        if (workRequest.getWorkSpecialization() != null) {
            foundWorkRequest.setWorkSpecialization(workRequest.getWorkSpecialization());
        }
        if (workRequest.getCommercialOffers() != null) {
            foundWorkRequest.setCommercialOffers(workRequest.getCommercialOffers());
        }
        return new ResponseEntity<>(workRequestRepository.save(foundWorkRequest), HttpStatus.ACCEPTED);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCustomer(@PathVariable("id") Long id) {
        try {
            workRequestRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
        }
    }

}
