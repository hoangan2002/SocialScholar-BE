package com.social.app.model;

import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long postId;

    private String content;
    private Date time;
    private String imageURL;
    @ManyToOne
    @JoinColumn(name="userId")
    private User user;

    @ManyToOne
    @JoinColumn(name="groupId")
    private Group group;

//    @OneToMany(mappedBy = "post")
//    private List<Comment> comments;
//
//    @OneToMany(mappedBy = "post")
//    private List<PostReport> reports;
//
//    @OneToMany(mappedBy = "post")
//    private List<PostLike> likes;





}