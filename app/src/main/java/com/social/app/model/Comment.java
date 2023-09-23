package com.social.app.model;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long commentId;
    private String content;
    private Timestamp time;

    @ManyToOne
    @JoinColumn(name="post_Id")
    private Post post;

    @ManyToOne
    @JoinColumn(name="user_Id")
    private  User user;

    @OneToMany(mappedBy = "commentReport")
    private List<CommentReport> reports;

    @OneToMany(mappedBy = "commentLike")
    private List<CommentLike> likes;

}
