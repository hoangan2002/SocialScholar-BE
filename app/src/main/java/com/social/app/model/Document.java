package com.social.app.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Document")
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long documentId;
    private String documentName;
    private String description;
    private String url;
    private int cost;
    private boolean isApproved;
    private Timestamp time;

    @JsonBackReference(value = "document_groups")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="groupId")
    private Groups group;

    @JsonBackReference(value = "document_user")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="user_Id")
    private User author;

    @JsonManagedReference(value = "bill_document")
    @OneToMany(mappedBy = "document")
    private List<Bill> bills;


    @OneToMany(mappedBy = "document")
    private List<Rating> ratings;
}
