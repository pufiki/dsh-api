package ru.digitalsuperhero.dshapi.controllers;

import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import ru.digitalsuperhero.dshapi.dao.domain.Customer;

public class CustomerResourceAssembler extends ResourceAssemblerSupport<Customer, CustomerResource> {
    public CustomerResourceAssembler() {
        super(CustomerController.class, CustomerResource.class);
    }

    @Override
    public CustomerResource instantiateResource(Customer customer) {
        return new CustomerResource(customer);
    }

    @Override
    public CustomerResource toResource(Customer customer) {
        return createResourceWithId(customer.getId(), customer);
    }
}
