package com.social.app.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "Join_Management")
public class JoinManagement {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long joinId;
    @ManyToOne
    @JoinColumn(name="groupId")
    private Groups group;

    @ManyToOne
    @JoinColumn(name="userId")
    private User user;

    private Date time;
}
