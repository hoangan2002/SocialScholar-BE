package com.social.app.model;

import jakarta.persistence.*;

import java.util.Date;

public class JoinManagement {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long joinId;
    @ManyToOne
    @JoinColumn(name="groupId")
    private Group group;

    @ManyToOne
    @JoinColumn(name="userId")
    private User user;

    private Date time;
}
