package ru.digitalsuperhero.dshapi.controllers;

import lombok.Getter;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;
import ru.digitalsuperhero.dshapi.dao.domain.Customer;
import ru.digitalsuperhero.dshapi.dao.domain.WorkRequest;

import java.util.List;

@Relation(value = "customer", collectionRelation = "customers")
public class CustomerResource extends ResourceSupport {
    @Getter
    private String companyName;
    @Getter
    private String email;
    @Getter
    private String password;
    @Getter
    private List<WorkRequest> workRequestsCreated;

    public CustomerResource(Customer customer) {
        this.companyName = customer.getName();
        this.email = customer.getEmail();
        this.password = customer.getPassword();
        this.workRequestsCreated = customer.getWorkRequestsCreated();
    }
}
