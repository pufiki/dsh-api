package ru.digitalsuperhero.dshapi.controllers;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.digitalsuperhero.dshapi.dao.CustomerRepository;
import ru.digitalsuperhero.dshapi.dao.domain.Customer;

import java.util.List;


@RepositoryRestController
@CrossOrigin(origins = "*") //?
public class CustomerController {
    private CustomerRepository customerRepository;

    public CustomerController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @GetMapping(path = "/customers", produces = "application/json")
    public Resources<CustomerResource> getCustomers() {
        PageRequest pageRequest =
                PageRequest.of(0, 6, Sort.by("id").descending());
        List<Customer> allCustomers = customerRepository.findAll(pageRequest).getContent();

        List<CustomerResource> customerResources =
                new CustomerResourceAssembler().toResources(allCustomers);
        Resources<CustomerResource> recentResources =
                new Resources<CustomerResource>(customerResources);
//        recentResources.add(
//                linkTo(methodOn(CustomerController.class).getCustomers())
//                        .withRel("recents"));
        return recentResources;
    }

    @PutMapping(path = "customers/{id}", consumes = "application/json")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Customer putCustomer(@PathVariable("id") Long id, @RequestBody Customer customer) {
        //TODO: fix
        Customer foundCustomer = customerRepository.findById(id).get();
        customerRepository.delete(customer);
        customer.setId(id);
        return customerRepository.save(customer);
    }

    @PatchMapping(path = "customers/{id}", consumes = "application/json")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Customer patchCustomer(@PathVariable("id") Long id, @RequestBody Customer customerPatch) {
        //TODO: fix
        Customer foundCustomer = customerRepository.findById(id).get();
        if (customerPatch.getPassword() != null) {
            foundCustomer.setPassword(customerPatch.getPassword());
        }
        if (customerPatch.getEmail() != null) {
            foundCustomer.setEmail(customerPatch.getEmail());
        }
        if (customerPatch.getCompanyName() != null) {
            foundCustomer.setCompanyName(customerPatch.getCompanyName());
        }
        if (customerPatch.getWorkRequestsCreated() != null) {
            foundCustomer.setWorkRequestsCreated(customerPatch.getWorkRequestsCreated());
        }
        return customerRepository.save(foundCustomer);
    }

    @DeleteMapping(path = "customers/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCustomer(@PathVariable("id") Long id) {
        try {
            customerRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
        }
    }
}
