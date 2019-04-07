package ru.digitalsuperhero.dshapi.security;

import lombok.Data;
import ru.digitalsuperhero.dshapi.dao.domain.Customer;

@Data
public class CustomerAuthenticatedResponse extends Customer {
    private final String token;

    public CustomerAuthenticatedResponse(Customer customer, String token) {
        this.token = token;
        super.setId(customer.getId());
        super.setPassword(customer.getPassword());
        super.setName(customer.getName());
        super.setEmail(customer.getEmail());
        super.setWorkRequestsCreated(customer.getWorkRequestsCreated());
    }
}
