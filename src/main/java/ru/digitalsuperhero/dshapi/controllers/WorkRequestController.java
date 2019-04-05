package ru.digitalsuperhero.dshapi.controllers;


import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
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

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCustomer(@PathVariable("id") Long id) {
        try {
            workRequestRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
        }
    }

}
