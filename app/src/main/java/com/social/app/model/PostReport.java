package com.social.app.model;

import jakarta.persistence.*;

import java.util.Date;

public class PostReport {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long reportId;
    private String type;
    private String description;
    private Date time;

    @ManyToOne
    @JoinColumn(name="userId")
    private User user;

    @ManyToOne
    @JoinColumn(name="postId")
    private Post post;
}
