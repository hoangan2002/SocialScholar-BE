package com.social.app.model;

import jakarta.persistence.*;

import java.util.Date;

public class CommentLike {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long likeId;

    private Date time;

    @ManyToOne
    @JoinColumn(name="userId")
    private User user;

    @ManyToOne
    @JoinColumn(name="commentId")
    private Comment comment;

}
