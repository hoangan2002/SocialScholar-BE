package com.social.app.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "Bill")
@AllArgsConstructor
@NoArgsConstructor
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long billId;
    private Timestamp time;

    @ManyToOne
    @JsonBackReference(value = "bill_document")
    @JoinColumn(name="document_Id")
    private Document document;

    @ManyToOne
    @JsonBackReference(value = "bill_user")
    @JoinColumn(name="user_Id")
    private User user;

}
