package com.social.app.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "Comment_Like")
public class CommentLike {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long likeId;

    private Date time;


    private byte status;

    @ManyToOne
    @JoinColumn(name="user_Id")
    private User user;

    @ManyToOne
    @JoinColumn(name="comment_Id")
    private Comment commentLike;

}
