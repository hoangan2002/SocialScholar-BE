package com.social.app.model;

import jakarta.persistence.*;

import java.util.Date;

public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long billId;
    private Date time;
    @ManyToOne
    @JoinColumn(name="documentId")
    private Document document;

    @ManyToOne
    @JoinColumn(name="userId")
    private User user;

}
