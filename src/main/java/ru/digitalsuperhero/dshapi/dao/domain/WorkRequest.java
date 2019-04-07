package ru.digitalsuperhero.dshapi.dao.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.rest.core.annotation.RestResource;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table
@RestResource(rel = "workRequests", path = "workRequests")
public class WorkRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String name;
    private String description;
    private Date createdAt;
    private String imageUrl;
    @OneToOne(targetEntity = Customer.class)
    private Customer customer;
    private Boolean isClosed;
    private Integer state;
    @OneToOne
    private Discussion discussion;
    @OneToOne
    private Contractor contractor;

    public WorkRequest(Long id, String name, String description, Boolean isClosed, Customer customer, Integer state) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.isClosed = isClosed;
        this.customer = customer;
        this.state = state;
    }

    @PrePersist
    void createdAt() {
        this.createdAt = new Date();
    }
}
