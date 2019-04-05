package ru.digitalsuperhero.dshapi.dao.domain;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table
public class CommercialOffer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String description;
    @OneToOne(targetEntity = Discussion.class)
    private Discussion discussion;
    /**
     * The work request object this commercial offer is applied to.
     */
    @OneToOne(targetEntity = WorkRequest.class)
    private WorkRequest workRequest;
    /**
     * Review is created only after complete execution of WorkRequest;
     */
    @OneToOne(targetEntity = Review.class)
    private Review review;
}
