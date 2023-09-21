package com.social.app.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "Post_Report")
public class PostReport {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long reportId;
    private String type;
    private String description;
    private Date time;

    @ManyToOne
    @JoinColumn(name="user_Id")
    private User user;

    @ManyToOne
    @JoinColumn(name="post_Id")
    private Post post;
}
