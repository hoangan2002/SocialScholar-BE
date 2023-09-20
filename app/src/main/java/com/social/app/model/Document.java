package com.social.app.model;

import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long documentId;
    private String documentName;
    private String description;
    private String url;
    private int cost;
    private boolean isApproved;
    private Date time;

    @ManyToOne
    @JoinColumn(name="groupId")
    private Group group;

    @ManyToOne
    @JoinColumn(name="userId")
    private User author;

    @OneToMany(mappedBy = "document")
    private List<Bill> bills;

    @OneToMany(mappedBy = "document")
    private List<Rating> ratings;
}
