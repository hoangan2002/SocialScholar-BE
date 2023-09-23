package com.social.app.model;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Post")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long postId;

    private String content;
    private Timestamp time;
    private String imageURL;
    @ManyToOne
    @JoinColumn(name="user_Id")
    private User user;

    @ManyToOne
    @JoinColumn(name="group_Id")
    private Groups group;

    @OneToMany(mappedBy = "post")
    private List<Comment> comments;

    @OneToMany(mappedBy = "post")
    private List<PostReport> reports;
//
    @OneToMany(mappedBy = "post")
    private List<PostLike> likes;





}