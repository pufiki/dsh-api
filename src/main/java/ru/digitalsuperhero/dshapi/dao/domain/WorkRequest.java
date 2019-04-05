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
@RestResource(rel = "workRequests", path = "workRequests")
public class WorkRequest {

    public WorkRequest (Long id, String name, String description){
        this.id = id;
        this.name  = name;
        this.description = description;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String name;
    private String description;
    @OneToMany(targetEntity = Discussion.class)
    private List<Discussion> discussions;
    @OneToMany(targetEntity = CommercialOffer.class)
    private List<CommercialOffer> commercialOffers;
}
