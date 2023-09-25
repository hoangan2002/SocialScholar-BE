package com.social.app.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "Comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long commentId;
    private String content;
    private Timestamp time;

    @JsonBackReference(value = "post_comment")
    @ManyToOne
    @JoinColumn(name="post_Id")
    private Post post;

    @JsonBackReference(value = "comment_user")
    @ManyToOne
    @JoinColumn(name="user_Id")
    private  User user;


    @OneToMany(mappedBy = "commentReport")
    private List<CommentReport> reports;

    @OneToMany(mappedBy = "commentLike")
    private List<CommentLike> likes;

}
