package ru.digitalsuperhero.dshapi.dao.domain;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    private String description;
    /**
     * Rating stars from 1 to 5.
     */
    private Integer ratingStars;
}
