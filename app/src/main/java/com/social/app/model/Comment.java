package com.social.app.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import lombok.Data;


import java.sql.Timestamp;
import java.util.List;

@Entity
@Getter
@Setter
@Data
@Table(name = "Comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long commentId;

    private String content;
    private Timestamp time;
    private long commentParentId;

    @JsonBackReference(value = "post_comment")
    @ManyToOne
    @JoinColumn(name="post_Id")
    private Post postId;

    @JsonBackReference(value = "comment_user")
    @ManyToOne
    @JoinColumn(name="user_Id")
    private User user;

    @JsonManagedReference(value = "comment_report")
    @OneToMany(mappedBy = "comment", cascade = CascadeType.REMOVE)
    private List<CommentReport> reports;

    @JsonManagedReference(value = "comment_like")
    @OneToMany(mappedBy = "comment", cascade = CascadeType.REMOVE)
    private List<CommentLike> likes;

}
