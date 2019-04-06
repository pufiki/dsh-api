package ru.digitalsuperhero.dshapi.security;

import lombok.Data;
import ru.digitalsuperhero.dshapi.dao.domain.Contractor;

@Data
public class ContractorAuthenticatedResponse extends Contractor {

    private final String token;

    public ContractorAuthenticatedResponse(Contractor contractor, String token) {
        this.token = token;
        super.setId(contractor.getId());
        super.setPassword(contractor.getPassword());
        super.setEmail(contractor.getEmail());
        super.setCompanyName(contractor.getEmail());
        super.setRating(contractor.getRating());
        super.setWorkSpecialization(contractor.getWorkSpecialization());
    }
}
