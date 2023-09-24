package com.social.app.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "Login_History")
public class LoginHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long loHistoryId;

    private Date time;

    @ManyToOne
    @JoinColumn(name="userId")
    private User user;
}
