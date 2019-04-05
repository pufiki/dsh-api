package ru.digitalsuperhero.dshapi.dao.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long userId;
    private Date sentAt;

    @PrePersist
    void sentAt() {
        this.sentAt = new Date();
    }
}
