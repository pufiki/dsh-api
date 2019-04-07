package ru.digitalsuperhero.dshapi.dao.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.rest.core.annotation.RestResource;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = "email")})
@RestResource(rel = "customers", path = "customers")
public class Customer{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String email;
    private String description;
    private String phoneNumber;
    private String password;
    private String photoUrl;
    private Boolean signedIn = false;
    @OneToMany(targetEntity = WorkRequest.class)
    private List<WorkRequest> workRequestsCreated;

    public Customer(String name, String email, String phoneNumber, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
    }
}
