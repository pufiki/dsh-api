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
    @OneToMany(targetEntity = CommercialOffer.class)
    private List<CommercialOffer> commercialOffers;
    private Boolean isClosed;

    public WorkRequest(Long id, String name, String description, Boolean isClosed) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.isClosed = isClosed;
    }

    @PrePersist
    void createdAt() {
        this.createdAt = new Date();
    }
}
