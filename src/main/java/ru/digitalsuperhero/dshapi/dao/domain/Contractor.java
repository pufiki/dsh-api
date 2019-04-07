package ru.digitalsuperhero.dshapi.dao.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.rest.core.annotation.RestResource;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table
@RestResource(rel = "contractors", path = "contractors")
public class Contractor {
    @OneToMany
    List<Review> reviews;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String companyName;
    private String email;
    private String password;
    private Boolean signedIn = false;
    @OneToMany(targetEntity = WorkRequest.class)
    private List<WorkRequest> workRequests;
    /**
     * Contractor rating stars from 1.0 to 5.0 based on proposed and executed commercial offers.
     */
    private Double rating;

    public Contractor(Long id, String companyName, String email, String password) {
        this.id = id;
        this.companyName = companyName;
        this.email = email;
        this.password = password;
    }
}
