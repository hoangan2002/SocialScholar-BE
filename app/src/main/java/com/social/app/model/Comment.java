package com.social.app.model;

import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long commentId;
    private String content;
    private Date time;

    @ManyToOne
    @JoinColumn(name="postId")
    private Post post;

    @ManyToOne
    @JoinColumn(name="userId")
    private  User user;

    @OneToMany(mappedBy = "comment")
    private List<CommentReport> reports;

    @OneToMany(mappedBy = "comment")
    private List<CommentLike> likes;

}
